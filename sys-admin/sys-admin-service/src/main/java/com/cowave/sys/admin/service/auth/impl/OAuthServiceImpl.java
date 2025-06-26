/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.HttpResponse;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.domain.auth.OAuthClient;
import com.cowave.sys.admin.domain.auth.bo.GitlabUser;
import com.cowave.sys.admin.domain.auth.OAuthServer;
import com.cowave.sys.admin.domain.auth.bo.GitlabToken;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.domain.auth.bo.OAuth2CodeBo;
import com.cowave.sys.admin.domain.auth.dto.OAuthUserDto;
import com.cowave.sys.admin.domain.auth.request.OAuth2CodeRequest;
import com.cowave.sys.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.sys.admin.domain.auth.vo.OAuth2CodeVo;
import com.cowave.sys.admin.domain.base.SysOperation;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.infra.auth.dao.LdapUserDao;
import com.cowave.sys.admin.infra.auth.dao.OAuthClientDao;
import com.cowave.sys.admin.infra.auth.dao.OAuthServerDao;
import com.cowave.sys.admin.infra.auth.dao.OAuthUserDao;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.OAuthUserDtoMapper;
import com.cowave.sys.admin.infra.base.SysOperationHandler;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.service.auth.GitlabService;
import com.cowave.sys.admin.service.auth.OAuthService;
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

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.INTERNAL_SERVER_ERROR;
import static com.cowave.sys.admin.domain.AdminRedisKeys.AUTH_OAUTH;
import static com.cowave.sys.admin.domain.auth.AuthType.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private final ApplicationProperties applicationProperties;
    private final BearerTokenService bearerTokenService;
    private final RedisHelper redisHelper;
    private final SysUserDao sysUserDao;
    private final LdapUserDao ldapUserDao;
    private final OAuthUserDao oauthUserDao;
    private final OAuthClientDao oAuthClientDao;
    private final OAuthServerDao oauthServerDao;
    private final OAuthUserDtoMapper oauthUserDtoMapper;
    private final SysRoleDtoMapper sysRoleDtoMapper;
    private final SysOperationHandler sysOperationHandler;
    private final GitlabService gitlabService;

    @Override
    public AccessUserDetails gitlabCallback(String code) {
        // 根据授权码兑换令牌
        OAuthServer oAuthServer = oauthServerDao.getByServerType(GITLAB.val());

        HttpResponse<GitlabToken> tokenResponse = gitlabService.getGitlabToken(oAuthServer.getAuthUrl(),
                oAuthServer.getAppId(), oAuthServer.getAppSecret(),
                oAuthServer.getRedirectUrl(), oAuthServer.getGrantType(),
                oAuthServer.getAuthScope(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());

        GitlabToken gitlabToken = tokenResponse.getBody();
        assert gitlabToken != null;
        HttpResponse<GitlabUser> userResponse = gitlabService.getGitlabUser(oAuthServer.getAuthUrl(), gitlabToken.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        GitlabUser gitlabUser = userResponse.getBody();

        // 保存Gitlab用户
        assert gitlabUser != null;
        OAuthUser oauthUser = oauthUserDao.getByAccount(gitlabUser.getUsername(), GITLAB.val());
        if (oauthUser != null) {
            oauthUser.setUserName(gitlabUser.getName());
            oauthUser.setUserEmail(gitlabUser.getEmail());
            oauthUser.setUserAvatar(gitlabUser.getAvatarUrl());
            oauthUser.setUpdateTime(new Date());
            if (CollectionUtils.isNotEmpty(gitlabUser.getIdentities())) {
                GitlabUser.LdapInfo ldap = gitlabUser.getIdentities().get(0);
                oauthUser.setUserDept(ldap.getExternUid());
            }
            oauthUserDao.updateById(oauthUser);
        } else {
            oauthUser = GitlabUser.oAuthUser(gitlabUser);
            oauthUser.setUserCode(GITLAB.generateCode());
            oauthUser.setRoleCode(oAuthServer.getRoleCode());
            oauthUserDao.save(oauthUser);
        }

        // 使用Gitlab用户信息构造当前系统的访问令牌
        String roleCode = oauthUser.getRoleCode();
        List<String> permits;
        if(Permission.ROLE_ADMIN.equals(roleCode)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = sysRoleDtoMapper.getPermitsByRoleCode(roleCode);
        }

        AccessUserDetails userDetails = oauthUser.newUserDetails();
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        userDetails.setPermissions(permits);
        userDetails.setRoles(List.of(roleCode));
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 登录日志
        OperationInfo operationInfo = new OperationInfo();
        operationInfo.setSuccess(true);
        operationInfo.setOpModule("op_admin");
        operationInfo.setOpType("op_auth");
        operationInfo.setOpAction("op_login");
        operationInfo.setDesc("Gitlab登录：" + oauthUser.getUserAccount());
        sysOperationHandler.create(operationInfo, null);
        return userDetails;
    }

    @Override
    public List<OAuthServer> getEnableServers() {
        return oauthServerDao.getEnableServers();
    }

    @Override
    public OAuthServer getServerConfig(String serverType) {
        return oauthServerDao.getByServerType(serverType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateServerConfig(OAuthServer oauthServer) {
        oauthServerDao.removeByServerType(oauthServer.getServerType());
        oauthServerDao.save(oauthServer);
    }

    @Override
    public List<OAuthUserDto> listUser(OAuthUserQuery query) {
        return oauthUserDtoMapper.listUser(Access.page(), query);
    }

    @Override
    public OAuthUser infoUser(Integer id) {
        return oauthUserDao.getById(id);
    }

    @Override
    public void deleteUser(Integer userId) {
        oauthUserDao.removeById(userId);
    }

    @Override
    public void updateUserRole(Integer userId, String roleCode) {
        oauthUserDao.updateRoleCodeById(userId, roleCode);
    }

    @Override
    public Page<OAuthClient> listClient(String clientName) {
        return oAuthClientDao.queryPage(clientName);
    }

    @Override
    public OAuthClient createClient(OAuthClient oAuthClient) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        String clientSecret = UUID.randomUUID().toString().replace("-", "");
        oAuthClient.setClientId(clientId);
        oAuthClient.setClientSecret(clientSecret);
        oAuthClientDao.save(oAuthClient);
        return oAuthClient;
    }

    @Override
    public void deleteClient(List<Integer> ids) {
        oAuthClientDao.removeByIds(ids);
    }

    @Override
    public OAuth2CodeVo getClientCode(OAuth2CodeRequest request) {
        // 验证客户端id
        OAuthClient oAuthClient = oAuthClientDao.getByClientId(request.getClientId());
        HttpAsserts.notNull(oAuthClient, BAD_REQUEST, "{admin.oauth.name.notexist}");

        // 校验返回类型
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase("code", request.getResponseType()),
                BAD_REQUEST, "{admin.oauth.resp.invalid}");

        // 验证授权类型
        HttpAsserts.isTrue(oAuthClient.getGrantType().contains("authorization_code"),
                BAD_REQUEST, "{admin.oauth.grant.invalid}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oAuthClient.getRedirectUrl(), request.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 生成随机code
        String code = UUID.randomUUID().toString().replace("-", "");

        OAuth2CodeBo oAuth2CodeBo = new OAuth2CodeBo();
        oAuth2CodeBo.setUserCode(Access.userCode());
        oAuth2CodeBo.setState(request.getState());
        oAuth2CodeBo.setRedirectUri(oAuthClient.getRedirectUrl());

        // PKCE校验（这里简单示意下只支持md5）
        if(StringUtils.isNotBlank(request.getCodeChallenge())
                && "md5".equalsIgnoreCase(request.getCodeChallengeMethod())){
            String codeVerifier = SecureUtil.md5(request.getCodeChallenge());
            oAuth2CodeBo.setCodeVerifier(codeVerifier);
        }

        // 绑定用户信息
        redisHelper.putExpire(AUTH_OAUTH.formatted(code), oAuth2CodeBo, 60, TimeUnit.SECONDS);
        return new OAuth2CodeVo(code, oAuthClient.getClientName(), oAuthClient.getAuthScope());
    }

    @Override
    public void clientRedirect(String code, HttpServletResponse response) throws IOException {
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(AUTH_OAUTH.formatted(code));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");
        // 回调
        String redirectUrl = oAuth2CodeBo.getRedirectUri() + "?code=" + code + "&state=" + oAuth2CodeBo.getState();
        response.sendRedirect(redirectUrl);
    }

    @Override
    public AccessUserDetails getClientToken(OAuth2TokenRequest request) {
        // 获取用户code
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(AUTH_OAUTH.formatted(request.getCode()));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");

        // 验证客户端id
        OAuthClient oAuthClient = oAuthClientDao.getByClientId(request.getClientId());
        HttpAsserts.notNull(oAuthClient, BAD_REQUEST, "{admin.oauth.name.notexist}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oAuthClient.getRedirectUrl(), request.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 验证客户端密钥
        HttpAsserts.isTrue(StringUtils.equals(request.getClientSecret(), oAuthClient.getClientSecret()),
                BAD_REQUEST, "{admin.oauth.secret.invalid}");

        // PKCE校验
        if(StringUtils.isNotBlank(oAuth2CodeBo.getCodeVerifier())){
            HttpAsserts.isTrue(StringUtils.equals(oAuth2CodeBo.getCodeVerifier(), request.getCodeVerifier()),
                BAD_REQUEST, "{admin.oauth.pkce.invalid}");
        }

        // 创建令牌
        AccessUserDetails userDetails;
        String userCode = oAuth2CodeBo.getUserCode();
        if (GITLAB.equalsType(userCode)) {
            OAuthUser oAuthUser = oauthUserDao.getByUserCode(userCode);
            userDetails = oAuthUser.newUserDetails();
            userDetails.setAuthType(OAUTH.val() + "/" + GITLAB.val());
        } else if (LDAP.equalsType(userCode)) {
            LdapUser ldapUser = ldapUserDao.getByUserCode(userCode);
            userDetails = ldapUser.newUserDetails();
            userDetails.setAuthType(OAUTH.val() + "/" + LDAP.val());
        } else {
            SysUser sysUser = sysUserDao.getByUserCode(userCode);
            userDetails = sysUser.newUserDetails(null);
            userDetails.setAuthType(OAUTH.val() + "/" + SYS.val());
        }
        // 根据scope设置permits，这里就省略了（当前登录的用户信息直接可以获取）
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 授权日志
        SysOperation sysOperation = new SysOperation();
        sysOperation.setOpStatus(1);
        sysOperation.setOpModule("op_admin");
        sysOperation.setOpType("op_auth");
        sysOperation.setOpAction("op_oauth");
        sysOperation.setOpDesc("授权应用'" + oAuthClient.getClientName() + "'访问");
        sysOperation.setAccess(new AccessInfo(userDetails));
        sysOperation.setIp(Access.accessIp());
        sysOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        sysOperation.setOpTime(Access.accessTime());
        sysOperationHandler.create(sysOperation);
        return userDetails;
    }
}
