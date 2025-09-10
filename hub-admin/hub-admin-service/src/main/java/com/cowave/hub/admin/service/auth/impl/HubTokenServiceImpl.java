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

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.AccessProperties;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.tools.Collections;
import com.cowave.commons.tools.NetUtils;
import com.cowave.hub.admin.domain.auth.HubToken;
import com.cowave.hub.admin.domain.auth.HubTokenMenu;
import com.cowave.hub.admin.domain.auth.request.TokenRequest;
import com.cowave.hub.admin.domain.auth.vo.TokenVo;
import com.cowave.hub.admin.domain.rabc.dto.MenuScopeDto;
import com.cowave.hub.admin.infra.auth.dao.HubTokenDao;
import com.cowave.hub.admin.infra.auth.dao.HubTokenMenuDao;
import com.cowave.hub.admin.infra.rabc.dao.HubMenuDao;
import com.cowave.hub.admin.service.auth.HubTokenService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cowave.commons.framework.access.security.BearerTokenService.*;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_API;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_API_CURRENT;
import static com.cowave.hub.admin.domain.auth.enums.AuthType.API;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubTokenServiceImpl implements HubTokenService {
    private final ApplicationProperties applicationProperties;
    private final AccessProperties accessProperties;
    private final HubMenuDao hubMenuDao;
    private final HubTokenDao hubTokenDao;
    private final HubTokenMenuDao hubTokenMenuDao;
    private final RedisHelper redisHelper;

    @PostConstruct
    public void indexApiToken() {
        List<HubToken> list = hubTokenDao.list();
        for (HubToken hubToken : list) {
            setApiTokenToUse(hubToken);
        }
    }

    private void setApiTokenToUse(HubToken hubToken) {
        List<NetUtils.IpMask> ipRules = parseIpMask(hubToken.getIpRule());
        if (hubToken.getExpire() != null) {
            long expire = hubToken.getExpire().getTime() - System.currentTimeMillis();
            if (expire > 0) {
                redisHelper.putExpire(AUTH_API.formatted(hubToken.getTokenId()), ipRules, expire, TimeUnit.MILLISECONDS);
            }
        } else {
            redisHelper.putValue(AUTH_API.formatted(hubToken.getTokenId()), ipRules);
        }
    }

    private List<NetUtils.IpMask> parseIpMask(String ipRule){
        if (StringUtils.isBlank(ipRule)){
            return new ArrayList<>();
        }

        List<NetUtils.IpMask> list = new ArrayList<>();
        String[] array = ipRule.split(",");
        for(String ip : array){
            list.add(new NetUtils.IpMask(ip));
        }
        return list;
    }

    @Override
    public List<TokenVo> listApiToken(){
        List<HubToken> tokenList = hubTokenDao.listByUserCode(Access.userCode());
        List<TokenVo> list = Collections.convertToList(tokenList, TokenVo.class);
        for(TokenVo tokenVo : list){
            Map<String, Object> accessInfo = redisHelper.getValue(AUTH_API_CURRENT.formatted(tokenVo.getTokenId()));
            if(accessInfo != null){
                tokenVo.setAccessIp((String)accessInfo.get("ip"));
                tokenVo.setAccessUrl((String)accessInfo.get("url"));
                tokenVo.setAccessTime((Date)accessInfo.get("time"));
            }
            tokenVo.setMenuIds(hubTokenMenuDao.listMenusByTokenId(tokenVo.getTokenId()));
        }
        return list;
    }

    @Override
    public String creatApiToken(TokenRequest request) {
        // 先保存令牌
        hubTokenDao.save(request);

        // 令牌菜单
        List<String> permits = new ArrayList<>();
        List<MenuScopeDto> menuScopes = request.getMenuScopes();
        if(CollectionUtils.isNotEmpty(menuScopes)){
            permits = hubMenuDao.queryPermitsByIds(Collections.copyToList(menuScopes, MenuScopeDto::getMenuId));
        }

        // 用户信息
        AccessUserDetails userDetails = Access.userDetails();
        // 用户角色（去除系统管理员）
        List<String> roles = userDetails.getRoles();
        if(CollectionUtils.isNotEmpty(roles)){
            roles.remove(Permission.ROLE_ADMIN);
        }

        // 构造令牌
        JwtBuilder jwtBuilder = Jwts.builder()
                .claim(CLAIM_ACCESS_ID, String.valueOf(request.getTokenId()))
                .claim(CLAIM_TYPE, API.getVal())
                .claim(CLAIM_USER_ID, userDetails.getUserId())
                .claim(CLAIM_USER_CODE, userDetails.getUserCode())
                .claim(CLAIM_USER_PROPERTIES, userDetails.getUserProperties())
                .claim(CLAIM_USER_NAME, userDetails.getUserNick())
                .claim(CLAIM_USER_ACCOUNT, userDetails.getUsername())
                .claim(CLAIM_DEPT_ID, userDetails.getDeptId())
                .claim(CLAIM_DEPT_CODE, userDetails.getDeptCode())
                .claim(CLAIM_DEPT_NAME, userDetails.getDeptName())
                .claim(CLAIM_CLUSTER_ID, applicationProperties.getClusterId())
                .claim(CLAIM_CLUSTER_LEVEL, applicationProperties.getClusterLevel())
                .claim(CLAIM_CLUSTER_NAME, applicationProperties.getClusterName())
                .claim(CLAIM_USER_ROLE, roles)
                .claim(CLAIM_USER_PERM, permits)
                .claim(CLAIM_MULTIPLE, 1)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, accessProperties.accessSecret());
        if(request.getExpire() != null){
            jwtBuilder.setExpiration(request.getExpire());
        }
        String tokenValue = jwtBuilder.compact();

        // 更新令牌
        request.setTokenValue(tokenValue);
        hubTokenDao.updateById(request);

        // 保存令牌权限
        if(CollectionUtils.isNotEmpty(menuScopes)){
            List<HubTokenMenu> tokenMenus = Collections.copyToList(menuScopes,
                    v -> new HubTokenMenu(request.getTokenId(), v.getMenuId(), v.getScopeId()));
            hubTokenMenuDao.saveBatch(tokenMenus);
        }

        // 持久化到缓存
        setApiTokenToUse(request);
        return tokenValue;
    }

    /**
     * 删除用户令牌
     */
    @Override
    public void deleteApiToken(Integer tokenId) {
        hubTokenDao.removeById(tokenId);
        hubTokenMenuDao.removeByTokenId(tokenId);
        // 从缓存删除
        redisHelper.delete(AUTH_API.formatted(tokenId));
    }
}
