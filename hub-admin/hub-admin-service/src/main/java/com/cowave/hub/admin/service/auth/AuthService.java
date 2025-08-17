/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.HttpHintException;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.*;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.auth.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.request.RegisterRequest;
import com.cowave.hub.admin.domain.auth.vo.AuthInfo;
import com.cowave.hub.admin.domain.auth.vo.OnlineInfo;
import com.cowave.hub.admin.domain.sys.HubAttach;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.domain.rabc.*;
import com.cowave.hub.admin.domain.rabc.vo.Route;
import com.cowave.hub.admin.domain.rabc.vo.RouteMeta;
import com.cowave.hub.admin.infra.auth.MfaAuthVerifier;
import com.cowave.hub.admin.infra.auth.MfaConfiguration;
import com.cowave.hub.admin.infra.auth.dao.HubOAuthUserDao;
import com.cowave.hub.admin.infra.auth.dao.UserDetailsDao;
import com.cowave.hub.admin.infra.AdminOperationHandler;
import com.cowave.hub.admin.infra.sys.dao.HubConfigDao;
import com.cowave.hub.admin.infra.sys.mapper.dto.HubNoticeDtoMapper;
import com.cowave.hub.admin.infra.rabc.dao.HubRoleDao;
import com.cowave.hub.admin.infra.rabc.dao.HubTenantDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserRoleDao;
import com.cowave.hub.admin.service.sys.HubAttachService;
import com.cowave.hub.admin.service.rabc.HubMenuService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.FORBIDDEN;
import static com.cowave.commons.framework.access.security.BearerTokenService.CLAIM_TENANT_ID;
import static com.cowave.commons.framework.access.security.BearerTokenService.CLAIM_USER_ACCOUNT;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_FAILS;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_LOCK;
import static com.cowave.hub.admin.domain.sys.enums.AttachType.AVATAR;
import static com.cowave.hub.admin.domain.rabc.enums.EnableStatus.ENABLE;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGOUT_FORCE;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AuthService {
    private final RedisHelper redisHelper;
    private final AuthenticationManager authenticationManager;
    private final AdminOperationHandler operationHandler;
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;
    private final HubAttachService attachService;
    private final HubMenuService hubMenuService;
    private final HubTenantDao hubTenantDao;
    private final HubConfigDao hubConfigDao;
    private final HubUserDao hubUserDao;
    private final HubOAuthUserDao oauthUserDaoHub;
    private final HubRoleDao hubRoleDao;
    private final HubUserRoleDao hubUserRoleDao;
    private final HubNoticeDtoMapper hubNoticeDtoMapper;
    private final MfaConfiguration mfaConfiguration;
    private final UserDetailsDao userDetailsDao;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterRequest request) {
        String tenantId = request.getTenantId();

        boolean registerOnOff = hubConfigDao.getConfigValue(tenantId, "hub.registerOnOff");
        HttpAsserts.isTrue(registerOnOff, FORBIDDEN, "{admin.register.disable}");

        String userCode = UserType.SYS.newCode(tenantId, request.getUserAccount());
        String initPasswd = hubConfigDao.getConfigValue(tenantId, "hub.initPassword");
        HubUser hubUser = HubUser.builder()
                .tenantId(tenantId)
                .userType(UserType.SYS)
                .userStatus(ENABLE)
                .userCode(userCode)
                .userEmail(request.getUserEmail())
                .userName(request.getUserName())
                .userAccount(request.getUserAccount())
                .userPasswd(passwordEncoder.encode(initPasswd))
                .build();
        hubUserDao.save(hubUser);

        HubRole hubRole = hubRoleDao.getByCode(tenantId, "role-readonly");
        if(hubRole != null) {
            hubUserRoleDao.save(new HubUserRole(hubUser.getUserId(), hubRole.getRoleId()));
        }

        // 注册用户的通知消息
        hubNoticeDtoMapper.initNoticeMsgForNewUser(userCode);
        hubNoticeDtoMapper.updateNoticeStatForNewUser();
        return initPasswd;
    }

    /**
     * 登录
     */
    public AccessUserDetails login(String tenantId, String userAccount, String passwd) {
        Long lockTime = redisHelper.getExpire(AUTH_LOCK.formatted(tenantId, userAccount));
        if (lockTime != null && lockTime > 0) {
            long minutes = (lockTime + 59) / 60;
            throw new HttpHintException(BAD_REQUEST, "{admin.auth.locked}", minutes);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new TenantUsernamePasswordAuthenticationToken(tenantId, userAccount, passwd));
            AccessUserDetails userDetails = (AccessUserDetails) authentication.getPrincipal();
            bearerTokenService.assignAccessRefreshToken(userDetails);

            // 登录日志
            OperationInfo operationInfo = OperationInfo.builder()
                    .success(true)
                    .opModule(SYSTEM)
                    .opType(SYSTEM_AUTH)
                    .opAction(LOGIN)
                    .desc("用户登录：" + userAccount)
                    .build();
            operationHandler.create(operationInfo, null);
            return userDetails;
        } catch (BadCredentialsException e) {
            // 5min内最多允许尝试5次密码，否则锁定30min
            Long failCount = redisHelper.incrementValue(AUTH_FAILS.formatted(tenantId, userAccount), 1);
            if (failCount == 1) {
                redisHelper.expire(AUTH_FAILS.formatted(tenantId, userAccount), 300, TimeUnit.SECONDS);
            }
            if (failCount >= 5) {
                redisHelper.putExpire(AUTH_LOCK.formatted(tenantId, userAccount), "-", 1800, TimeUnit.SECONDS);
                throw new HttpHintException(BAD_REQUEST, "{admin.auth.locked}", 30);
            }
            throw new HttpHintException(BAD_REQUEST, "{admin.auth.failed}", 5 - failCount);
        }
    }

    /**
     * 二次认证
     */
    public AccessUserDetails mfa(String mfaToken, String mfaCode) {
        Claims claims = mfaConfiguration.parseMfaToken(mfaToken);
        String tenantId = (String) claims.get(CLAIM_TENANT_ID);
        String userAccount = (String) claims.get(CLAIM_USER_ACCOUNT);
        HubUser hubUser = hubUserDao.getByAccount(tenantId, UserType.SYS, userAccount);

        String mfaKey = hubUser.getMfa();
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(mfaKey, mfaCode), BAD_REQUEST, "{admin.mfa.code.invalid}");

        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        AccessUserDetails userDetails = userDetailsDao.newUserDetails(UserType.SYS.getVal(), hubTenant, hubUser);
        bearerTokenService.assignAccessRefreshToken(userDetails);
        return userDetails;
    }

    /**
     * 退出
     */
    public void logout() throws IOException {
        bearerTokenService.revoke();
    }

    /**
     * 在线用户
     */
    public List<OnlineInfo> onlineList() {
        List<AccessTokenInfo> list = bearerTokenService.listAccessToken(Access.tenantId());
        Map<String, List<AccessTokenInfo>> accessMap = com.cowave.commons.tools.Collections.groupToMap(list,
                access -> access.getAccessType() + access.getUserAccount());

        List<OnlineInfo> onlineList = new ArrayList<>();
        List<RefreshTokenInfo> refreshList = bearerTokenService.listRefreshToken(Access.tenantId());
        refreshList.sort(Comparator.comparing(RefreshTokenInfo::getLoginTime).reversed());
        for (RefreshTokenInfo refresh : refreshList) {
            List<AccessTokenInfo> accessList = accessMap.get(refresh.getAuthType() + refresh.getUserAccount());
            onlineList.add(OnlineInfo.builder()
                    .refreshId(refresh.getRefreshId())
                    .authType(refresh.getAuthType())
                    .userAccount(refresh.getUserAccount())
                    .userName(refresh.getUserName())
                    .cluster(refresh.getClusterName())
                    .loginIp(refresh.getLoginIp())
                    .loginTime(refresh.getLoginTime())
                    .accessList(accessList)
                    .build());
        }
        return onlineList;
    }

    /**
     * 撤销Access令牌
     */
    public void revokeAccess(String tenantId, String authType, String userAccount, String accessId) {
        AccessTokenInfo accessTokenInfo = bearerTokenService.revokeAccessToken(tenantId, authType, userAccount, accessId);
        if(accessTokenInfo == null){
            return;
        }
        // 强退日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGOUT_FORCE)
                .desc("强制退出：" + accessTokenInfo.getUserAccount())
                .build();
        operationHandler.create(operationInfo, null);
    }

    /**
     * 撤销Refresh令牌
     */
    public void revokeRefresh(String tenantId, String authType, String userAccount) {
        bearerTokenService.revokeRefreshToken(tenantId, authType, userAccount);
    }

    /**
     * 刷新令牌
     */
    public AccessUserDetails refresh(String refreshToken) throws Exception{
        return bearerTokenService.refreshAccessRefreshToken(refreshToken);
    }

    /**
     * 授权信息
     */
    public AuthInfo getAuthInfo() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        Integer userId = userDetails.getUserId();

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);
        authInfo.setUserName(userDetails.getUserNick());
        authInfo.setRoles(userDetails.getRoles());
        authInfo.setPermissions(userDetails.getPermissions());

        String tenantId = userDetails.getTenantId();
        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        authInfo.setTenantId(tenantId);
        authInfo.setTenantTitle(hubTenant.getTitle());
        authInfo.setTenantLogo(hubTenant.getLogo());

        // Avatar
        if (UserType.GITLAB.equalsVal(userDetails.getAuthType())) {
            HubOAuthUser hubOAuthUser =
                    oauthUserDaoHub.getByAccount(tenantId, UserType.GITLAB.getVal(), userDetails.getUsername());
            authInfo.setAvatar(hubOAuthUser.getUserAvatar());
        } else if (UserType.SYS.equalsVal(userDetails.getAuthType())) {
            HubAttach avatar = attachService.latestOfOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
            if (avatar != null) {
                authInfo.setAvatar(avatar.getViewUrl());
            }
        }
        return authInfo;
    }

    /**
     * 菜单权限
     */
    public List<Route> menus(){
        List<HubMenu> menuList;
        if(Access.isAdminUser()){
            menuList = hubMenuService.listMenusByAdmin(Access.tenantId());
        }else{
            List<String> userRoles = Access.userRoles();
            if(CollectionUtils.isEmpty(userRoles)){
                menuList = hubMenuService.listMenusInPublic(Access.tenantId());
            } else {
                menuList = hubMenuService.listMenusByRoles(Access.tenantId(), userRoles);
            }
        }

        if(menuList.isEmpty()){
            return Collections.emptyList();
        }

        List<HubMenu> rootMenus = new ArrayList<>();
        for(HubMenu menu : menuList){
            if (menu.getParentId() == 0) {
                recursionFn(menuList, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    private void recursionFn(List<HubMenu> list, HubMenu menu) {
        List<HubMenu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (HubMenu child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    private boolean hasChild(List<HubMenu> list, HubMenu t) {
        return !getChildList(list, t).isEmpty();
    }

    private List<HubMenu> getChildList(List<HubMenu> list, HubMenu parent) {
        List<HubMenu> children = new ArrayList<>();
        for (HubMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                children.add(child);
            }
        }
        return children;
    }

    private List<Route> buildRoutes(List<HubMenu> menus){
        List<Route> routes = new LinkedList<>();
        for (HubMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<HubMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && "M".equals(menu.getMenuType())) {
                route.setAlwaysShow(true);
                route.setRedirect("noRedirect");
                route.setChildren(buildRoutes(cMenus));
            } else if (menu.ifMenuFrame()) {
                route.setMeta(null);
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                children.setPath(menu.getMenuPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getMenuPath()));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));
                children.setQuery(menu.getMenuParam());
                childrenList.add(children);
                route.setChildren(childrenList);
            } else if (menu.getParentId() == 0L && menu.ifInnerLink()) {
                route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon()));
                route.setPath("/");
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                String routerPath = menu.getMenuPath();
                routerPath = routerPath.replace("http://", "");
                routerPath = routerPath.replace("https://", "");
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), menu.getMenuPath()));
                childrenList.add(children);
                route.setChildren(childrenList);
            }
            routes.add(route);
        }
        return routes;
    }
}
