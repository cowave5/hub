/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.filter.security.Permission;
import com.cowave.commons.framework.filter.security.TokenService;
import com.cowave.sys.admin.api.entity.oauth.*;
import com.cowave.sys.admin.api.mapper.OAuthMapper;
import com.cowave.sys.admin.api.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {

    private final RestTemplate restTemplate;

    private final TokenService tokenService;

    private final OAuthMapper oauthMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveConfig(OAuthConfig oAuthConfig) {
        oauthMapper.deleteConfig(oAuthConfig.getAppType());
        oauthMapper.saveConfig(oAuthConfig);
    }

    @Override
    public OAuthConfig getConfig(String appType) {
        return oauthMapper.getConfig(appType);
    }

    @Override
    public List<OAuthConfig> listConfig() {
        return oauthMapper.listConfig();
    }

    @Override
    public List<OAuthUser> listUser(OAuthUser oAuthUser) {
        return oauthMapper.listUser(oAuthUser);
    }

    @Override
    public OAuthUser infoUser(Long id) {
        return oauthMapper.infoUser(id);
    }

    @Override
    public void changeUserRole(Long userId, Long roleId) {
        oauthMapper.changeUserRole(userId, roleId, new Date());
    }

    @Override
    public void deleteUser(Long userId) {
        oauthMapper.deleteUser(userId);
    }

    @Override
    public AccessToken gitlabCallback(String code) {
        OAuthConfig oAuthConfig = oauthMapper.getConfig(OAuthConfig.APP_GITLAB);
        OAuthToken oauthToken = restTemplate.postForObject(oAuthConfig.gitlabTokenUrl(code), null, OAuthToken.class);
        assert oauthToken != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oauthToken.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<GitlabUser> response =
                restTemplate.exchange(oAuthConfig.gitlabUserUrl(), HttpMethod.GET, entity, GitlabUser.class);
        GitlabUser gitlabUser = response.getBody();
        assert gitlabUser != null;

        OAuthUser oAuthUser = GitlabUser.oAuthUser(gitlabUser);
        oAuthUser.setUserRole(oAuthConfig.getUserRole());
        oauthMapper.saveUser(oAuthUser); // 不更新role

        // 获取用户当前角色及对应权限
        String roleCode = oauthMapper.roleCode(oAuthUser.getId());
        List<String> permits;
        if(Permission.ROLE_ADMIN.equals(roleCode)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = oauthMapper.permits(oAuthUser.getId());
        }

        AccessToken accessToken = OAuthUser.accessToken(oAuthUser);
        accessToken.setPermissions(permits);
        accessToken.setRoles(List.of(roleCode));
        tokenService.assignToken(accessToken);
        return accessToken;
    }
}
