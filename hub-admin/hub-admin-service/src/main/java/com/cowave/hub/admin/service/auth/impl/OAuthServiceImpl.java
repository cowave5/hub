/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.HttpResponse;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.rabc.*;
import com.cowave.hub.admin.infra.auth.dao.HubOAuthAppDao;
import com.cowave.hub.admin.infra.auth.dao.HubOAuthDao;
import com.cowave.hub.admin.infra.auth.dao.HubOAuthUserDao;
import com.cowave.hub.admin.infra.auth.dao.UserDetailsDao;
import com.cowave.hub.admin.infra.rabc.dao.*;
import com.cowave.hub.admin.domain.auth.HubOAuthApp;
import com.cowave.hub.admin.domain.auth.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.HubOAuth;
import com.cowave.hub.admin.domain.auth.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.bo.OAuth2CodeBo;
import com.cowave.hub.admin.domain.auth.request.OAuth2CodeRequest;
import com.cowave.hub.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.hub.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.vo.OAuth2CodeVo;
import com.cowave.hub.admin.domain.sys.HubOperation;
import com.cowave.hub.admin.domain.rabc.enums.SuccessStatus;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.infra.AdminOperationHandler;
import com.cowave.hub.admin.service.auth.GitlabService;
import com.cowave.hub.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.AdminRedisKeys.*;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN_OAUTH;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private final BearerTokenService bearerTokenService;
    private final RedisHelper redisHelper;
    private final HubTenantDao hubTenantDao;
    private final HubUserDao hubUserDao;
    private final HubRoleDao hubRoleDao;
    private final HubUserRoleDao hubUserRoleDao;
    private final HubUserDiagramDao hubUserDiagramDao;
    private final HubOAuthDao hubOAuthDao;
    private final HubOAuthAppDao hubOAuthAppDao;
    private final HubOAuthUserDao oauthUserDaoHub;
    private final AdminOperationHandler operationHandler;
    private final GitlabService gitlabService;
    private final UserDetailsDao userDetailsDao;

    @Override
    public AccessUserDetails gitlabCallback(String tenantId, String code) {
        // 根据授权码兑换令牌
        HubOAuth oauth = hubOAuthDao.getByServerType(tenantId, UserType.GITLAB.getVal());

        HttpResponse<GitlabToken> tokenResponse = gitlabService.getGitlabToken(oauth.getAuthUrl(),
                oauth.getAppId(), oauth.getAppSecret(),
                oauth.getRedirectUrl(), oauth.getGrantType(),
                oauth.getAuthScope(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());

        GitlabToken gitlabToken = tokenResponse.getBody();
        assert gitlabToken != null;
        HttpResponse<GitlabUser> userResponse = gitlabService.getGitlabUser(oauth.getAuthUrl(), gitlabToken.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        GitlabUser gitlabUser = userResponse.getBody();

        // Gitlab用户信息
        assert gitlabUser != null;
        HubOAuthUser oauthUserHub = oauthUserDaoHub.getByAccount(tenantId, UserType.GITLAB.getVal(), gitlabUser.getUsername());
        if (oauthUserHub != null) {
            oauthUserHub.setUserName(gitlabUser.getName());
            oauthUserHub.setUserEmail(gitlabUser.getEmail());
            oauthUserHub.setUserAvatar(gitlabUser.getAvatarUrl());
            oauthUserHub.setUpdateTime(new Date());
            if (CollectionUtils.isNotEmpty(gitlabUser.getIdentities())) {
                GitlabUser.LdapInfo ldap = gitlabUser.getIdentities().get(0);
                oauthUserHub.setUserDept(ldap.getExternUid());
            }
            oauthUserDaoHub.updateById(oauthUserHub);
        } else {
            oauthUserHub = GitlabUser.oAuthUser(gitlabUser);
            oauthUserHub.setTenantId(tenantId);
            oauthUserDaoHub.save(oauthUserHub);
        }

        // 对应系统用户
        String userCode = UserType.GITLAB.newCode(tenantId, oauthUserHub.getUserAccount());
        HubUser hubUser = hubUserDao.getByCode(userCode);
        if(hubUser == null){
            hubUser = new HubUser();
            hubUser.setUserCode(userCode);
            hubUser.setTenantId(tenantId);
            hubUser.setUserType(UserType.GITLAB);
            hubUser.setUserAccount(oauthUserHub.getUserAccount());
            hubUser.setUserName(oauthUserHub.getUserName());
            hubUser.setUserEmail(oauthUserHub.getUserEmail());
            hubUserDao.save(hubUser);
            // role
            HubRole hubRole = hubRoleDao.getByCode(tenantId, oauth.getRoleCode());
            if(hubRole != null) {
                HubUserRole userRole = new HubUserRole(hubUser.getUserId(), hubRole.getRoleId());
                hubUserRoleDao.save(userRole);
            }
            // 用户关系
            HubUserDiagram userTree = new HubUserDiagram(hubUser.getUserId(), 0, tenantId);
            hubUserDiagramDao.save(userTree);
            // 用户树缓存
            redisHelper.delete(USER_DIAGRAM + ":" + tenantId);
            redisHelper.delete(DEPT_USER_DIAGRAM + ":" + tenantId);
        }else{
            hubUser.setUserName(oauthUserHub.getUserName());
            hubUser.setUserEmail(oauthUserHub.getUserEmail());
            hubUserDao.updateGitlabByCode(hubUser);
        }

        // 创建令牌
        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        AccessUserDetails userDetails = userDetailsDao.newUserDetails(UserType.GITLAB.getVal(), hubTenant, hubUser);
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 登录日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("Gitlab登录：" + oauthUserHub.getUserAccount())
                .build();
        operationHandler.create(operationInfo, null);
        return userDetails;
    }

    @Override
    public HubOAuth getOauth(String tenantId, String serverType) {
        return hubOAuthDao.getByServerType(tenantId, serverType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editOauth(String tenantId, HubOAuth oauth) {
        hubOAuthDao.removeByServerType(tenantId, oauth.getServerType());
        oauth.setTenantId(tenantId);
        hubOAuthDao.save(oauth);
    }

    @Override
    public Page<HubOAuthUser> listUser(String tenantId, OAuthUserQuery query) {
        return oauthUserDaoHub.getUserPage(tenantId, query.getServerType(), query.getUserAccount());
    }

    @Override
    public Page<HubOAuthApp> listClient(String tenantId, String clientName) {
        return hubOAuthAppDao.page(tenantId, clientName);
    }

    @Override
    public HubOAuthApp createClient(String tenantId, HubOAuthApp hubOAuthApp) {
        hubOAuthApp.setTenantId(tenantId);
        hubOAuthApp.setClientId(UUID.randomUUID().toString().replace("-", ""));
        hubOAuthApp.setClientSecret(UUID.randomUUID().toString().replace("-", ""));
        hubOAuthAppDao.save(hubOAuthApp);
        return hubOAuthApp;
    }

    @Override
    public void deleteClient(String tenantId, List<Integer> ids) {
        hubOAuthAppDao.removeByIds(tenantId, ids);
    }

    @Override
    public OAuth2CodeVo getClientCode(OAuth2CodeRequest request) {
        // 验证客户端id
        HubOAuthApp hubOAuthApp = hubOAuthAppDao.getByClientId(request.getClientId());
        HttpAsserts.notNull(hubOAuthApp, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 用户是否允许授权这个客户端
        HttpAsserts.equals(Access.tenantId(), hubOAuthApp.getTenantId(),
                FORBIDDEN, "{admin.oauth.resp.forbidden}");

        // 校验返回类型
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase("code", request.getResponseType()),
                BAD_REQUEST, "{admin.oauth.resp.invalid}");

        // 验证授权类型
        HttpAsserts.isTrue(hubOAuthApp.getGrantType().contains("authorization_code"),
                BAD_REQUEST, "{admin.oauth.grant.invalid}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(hubOAuthApp.getRedirectUrl(), request.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 生成随机code
        String code = UUID.randomUUID().toString().replace("-", "");

        OAuth2CodeBo oAuth2CodeBo = new OAuth2CodeBo();
        oAuth2CodeBo.setUserCode(Access.userCode());
        oAuth2CodeBo.setState(request.getState());
        oAuth2CodeBo.setRedirectUri(hubOAuthApp.getRedirectUrl());

        // PKCE校验（这里简单示意下只支持md5）
        if(StringUtils.isNotBlank(request.getCodeChallenge())
                && "md5".equalsIgnoreCase(request.getCodeChallengeMethod())){
            String codeVerifier = SecureUtil.md5(request.getCodeChallenge());
            oAuth2CodeBo.setCodeVerifier(codeVerifier);
        }

        // 绑定用户信息
        redisHelper.putExpire(OAUTH_CODE.formatted(code), oAuth2CodeBo, 60, TimeUnit.SECONDS);
        return new OAuth2CodeVo(code, hubOAuthApp.getClientName(), hubOAuthApp.getAuthScope());
    }

    @Override
    public void clientRedirect(String code, HttpServletResponse response) throws IOException {
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(OAUTH_CODE.formatted(code));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");
        // 回调
        String redirectUrl = oAuth2CodeBo.getRedirectUri() + "?code=" + code + "&state=" + oAuth2CodeBo.getState();
        response.sendRedirect(redirectUrl);
    }

    @Override
    public AccessUserDetails getClientToken(OAuth2TokenRequest request) {
        // 获取用户code
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(OAUTH_CODE.formatted(request.getCode()));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");

        // 验证客户端id
        HubOAuthApp hubOAuthApp = hubOAuthAppDao.getByClientId(request.getClientId());
        HttpAsserts.notNull(hubOAuthApp, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(hubOAuthApp.getRedirectUrl(), request.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 验证客户端密钥
        HttpAsserts.isTrue(StringUtils.equals(request.getClientSecret(), hubOAuthApp.getClientSecret()),
                BAD_REQUEST, "{admin.oauth.secret.invalid}");

        // PKCE校验
        if(StringUtils.isNotBlank(oAuth2CodeBo.getCodeVerifier())){
            HttpAsserts.isTrue(StringUtils.equals(oAuth2CodeBo.getCodeVerifier(), request.getCodeVerifier()),
                BAD_REQUEST, "{admin.oauth.pkce.invalid}");
        }

        // 创建令牌
        HubUser hubUser = hubUserDao.getByCode(oAuth2CodeBo.getUserCode());
        HubTenant hubTenant = hubTenantDao.getById(hubUser.getTenantId());
        String authType = hubUser.getUserType().oauthType();
        AccessUserDetails userDetails = userDetailsDao.newUserDetails(authType, hubTenant, hubUser);
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 授权日志
        HubOperation hubOperation = new HubOperation();
        hubOperation.setOpStatus(SuccessStatus.SUCCESS);
        hubOperation.setOpModule(SYSTEM);
        hubOperation.setOpType(SYSTEM_AUTH);
        hubOperation.setOpAction(LOGIN_OAUTH);
        hubOperation.setOpDesc("授权应用'" + hubOAuthApp.getClientName() + "'访问");
        hubOperation.setAccess(new AccessInfo(userDetails));
        hubOperation.setIp(Access.accessIp());
        hubOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        hubOperation.setOpTime(Access.accessTime());
        operationHandler.create(hubOperation);
        return userDetails;
    }
}
