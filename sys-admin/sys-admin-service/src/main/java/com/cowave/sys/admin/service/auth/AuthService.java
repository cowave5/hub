/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.HttpHintException;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessTokenInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.TenantUsernamePasswordAuthenticationToken;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.domain.auth.request.RegisterRequest;
import com.cowave.sys.admin.domain.auth.vo.AuthInfo;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.SysUserRole;
import com.cowave.sys.admin.domain.rabc.vo.Route;
import com.cowave.sys.admin.domain.rabc.vo.RouteMeta;
import com.cowave.sys.admin.infra.auth.MfaAuthVerifier;
import com.cowave.sys.admin.infra.auth.MfaConfiguration;
import com.cowave.sys.admin.infra.auth.UserDetailsServiceImpl;
import com.cowave.sys.admin.infra.auth.dao.OAuthUserDao;
import com.cowave.sys.admin.infra.base.SysOperationHandler;
import com.cowave.sys.admin.infra.base.dao.SysConfigDao;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysNoticeDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysRoleDao;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserRoleDao;
import com.cowave.sys.admin.service.base.SysAttachService;
import com.cowave.sys.admin.service.rabc.SysMenuService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.FORBIDDEN;
import static com.cowave.commons.framework.access.security.BearerTokenService.CLAIM_TENANT_ID;
import static com.cowave.commons.framework.access.security.BearerTokenService.CLAIM_USER_ACCOUNT;
import static com.cowave.sys.admin.domain.AdminRedisKeys.LOGIN_FAILS;
import static com.cowave.sys.admin.domain.AdminRedisKeys.LOGIN_LOCK;
import static com.cowave.sys.admin.domain.auth.AuthType.GITLAB;
import static com.cowave.sys.admin.domain.auth.AuthType.SYS;
import static com.cowave.sys.admin.domain.base.constants.AttachType.AVATAR;
import static com.cowave.sys.admin.domain.base.constants.OpAction.LOGIN;
import static com.cowave.sys.admin.domain.base.constants.OpAction.LOGOUT_FORCE;
import static com.cowave.sys.admin.domain.base.constants.OpModule.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AuthService {
    private final RedisHelper redisHelper;
    private final AuthenticationManager authenticationManager;
    private final SysOperationHandler sysOperationHandler;
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;
    private final SysAttachService attachService;
    private final SysMenuService sysMenuService;
    private final SysTenantDao sysTenantDao;
    private final SysConfigDao sysConfigDao;
    private final SysUserDao sysUserDao;
    private final OAuthUserDao oauthUserDao;
    private final SysRoleDao sysRoleDao;
    private final SysUserRoleDao sysUserRoleDao;
    private final SysNoticeDtoMapper sysNoticeDtoMapper;
    private final MfaConfiguration mfaConfiguration;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterRequest request) {
        String tenantId = request.getTenantId();

        boolean registerOnOff = sysConfigDao.getConfigValue(tenantId, "sys.registerOnOff");
        HttpAsserts.isTrue(registerOnOff, FORBIDDEN, "{admin.register.disable}");

        String userCode = sysTenantDao.nextUserCode(tenantId, SYS.val());
        String initPasswd = sysConfigDao.getConfigValue(tenantId, "sys.initPassword");
        SysUser sysUser = SysUser.builder()
                .tenantId(tenantId)
                .userType(SYS.val())
                .userStatus(1)
                .userCode(userCode)
                .userEmail(request.getUserEmail())
                .userName(request.getUserName())
                .userAccount(request.getUserAccount())
                .userPasswd(passwordEncoder.encode(initPasswd))
                .build();
        sysUserDao.save(sysUser);

        Integer readOnlyRoleId = sysRoleDao.queryIdByCode("role-readonly");
        sysUserRoleDao.save(new SysUserRole(sysUser.getUserId(), readOnlyRoleId));

        // 注册用户的通知消息
        sysNoticeDtoMapper.initNoticeMsgForNewUser(userCode);
        sysNoticeDtoMapper.updateNoticeStatForNewUser();
        return initPasswd;
    }

    /**
     * 登录
     */
    public AccessUserDetails login(String tenantId, String userAccount, String passwd) {
        Long lockTime = redisHelper.getExpire(LOGIN_LOCK.formatted(userAccount));
        if (lockTime != null && lockTime > 0) {
            long minutes = (lockTime + 59) / 60;
            throw new HttpHintException(BAD_REQUEST, "{admin.login.locked}", minutes);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new TenantUsernamePasswordAuthenticationToken(tenantId, userAccount, passwd));
            // 登录日志
            OperationInfo operationInfo = OperationInfo.builder()
                    .success(true)
                    .opModule(SYSTEM)
                    .opType(SYSTEM_AUTH)
                    .opAction(LOGIN)
                    .desc("用户登录：" + userAccount)
                    .build();
            sysOperationHandler.create(operationInfo, null);
            return (AccessUserDetails) authentication.getPrincipal();
        } catch (BadCredentialsException e) {
            // 5min内最多允许尝试5次密码，否则锁定30min
            Long failCount = redisHelper.incrementValue(LOGIN_FAILS.formatted(userAccount), 1);
            if (failCount == 1) {
                redisHelper.expire(LOGIN_FAILS.formatted(userAccount), 300, TimeUnit.SECONDS);
            }
            if (failCount >= 5) {
                redisHelper.putExpire(LOGIN_LOCK.formatted(userAccount), "-", 1800, TimeUnit.SECONDS);
                throw new HttpHintException(BAD_REQUEST, "{admin.login.locked}", 30);
            }
            throw new HttpHintException(BAD_REQUEST, "{admin.login.failed}", 5 - failCount);
        }
    }

    /**
     * 二次认证
     */
    public AccessUserDetails mfa(String mfaToken, String mfaCode) {
        Claims claims = mfaConfiguration.parseMfaToken(mfaToken);
        String tenantId = (String) claims.get(CLAIM_TENANT_ID);
        String userAccount = (String) claims.get(CLAIM_USER_ACCOUNT);
        SysUser sysUser = sysUserDao.getByAccount(tenantId, userAccount);

        String mfaKey = sysUser.getMfa();
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(mfaKey, mfaCode), BAD_REQUEST, "{admin.mfa.code.invalid}");

        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        return userDetailsService.buildUserDetails(sysUser, sysTenant);
    }

    /**
     * 退出
     */
    public void logout() throws IOException {
        bearerTokenService.revokeAccessRefreshToken();
    }

    /**
     * 强制退出
     */
    public void forceLogout(String tenantId, String accessId) {
        AccessTokenInfo accessTokenInfo = bearerTokenService.revokeAccessToken(tenantId, accessId);
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
        sysOperationHandler.create(operationInfo, null);
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
        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        authInfo.setTenantId(tenantId);
        authInfo.setTenantTitle(sysTenant.getTitle());
        authInfo.setTenantLogo(sysTenant.getLogo());

        if (GITLAB.equalsVal(userDetails.getAuthType())) {
            OAuthUser oAuthUser = oauthUserDao.getById(userId);
            authInfo.setUserEmail(oAuthUser.getUserEmail());
            authInfo.setAvatar(oAuthUser.getUserAvatar());
        } else if (SYS.equalsVal(userDetails.getAuthType())) {
            SysAttach avatar = attachService.latestOfOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
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
        List<SysMenu> menuList;
        if(Access.isAdminUser()){
            menuList = sysMenuService.listMenusByAdmin(Access.tenantId());
        }else{
            List<String> userRoles = Access.userRoles();
            if(CollectionUtils.isEmpty(userRoles)){
                menuList = sysMenuService.listMenusInPublic(Access.tenantId());
            } else {
                menuList = sysMenuService.listMenusByRoles(Access.tenantId(), userRoles);
            }
        }

        if(menuList.isEmpty()){
            return Collections.emptyList();
        }

        List<SysMenu> rootMenus = new ArrayList<>();
        for(SysMenu menu : menuList){
            if (menu.getParentId() == 0) {
                recursionFn(menuList, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    private void recursionFn(List<SysMenu> list, SysMenu menu) {
        List<SysMenu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (SysMenu child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return !getChildList(list, t).isEmpty();
    }

    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu parent) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                children.add(child);
            }
        }
        return children;
    }

    private List<Route> buildRoutes(List<SysMenu> menus){
        List<Route> routes = new LinkedList<>();
        for (SysMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<SysMenu> cMenus = menu.getChildren();
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
