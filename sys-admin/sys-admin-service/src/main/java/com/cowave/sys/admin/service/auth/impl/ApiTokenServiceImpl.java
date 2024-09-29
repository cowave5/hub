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

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.AccessProperties;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.tools.Collections;
import com.cowave.commons.tools.NetUtils;
import com.cowave.sys.admin.domain.auth.ApiToken;
import com.cowave.sys.admin.domain.auth.ApiTokenMenu;
import com.cowave.sys.admin.domain.auth.request.ApiTokenRequest;
import com.cowave.sys.admin.domain.auth.vo.ApiTokenVo;
import com.cowave.sys.admin.infra.auth.dao.ApiTokenDao;
import com.cowave.sys.admin.infra.auth.dao.ApiTokenMenuDao;
import com.cowave.sys.admin.infra.rabc.dao.SysMenuDao;
import com.cowave.sys.admin.service.auth.ApiTokenService;
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
import static com.cowave.sys.admin.domain.rabc.enums.AccessType.API;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ApiTokenServiceImpl implements ApiTokenService {
    private static final String PREFIX = "sys-admin:token:api:";
    private final ApplicationProperties applicationProperties;
    private final AccessProperties accessProperties;
    private final SysMenuDao sysMenuDao;
    private final ApiTokenDao apiTokenDao;
    private final ApiTokenMenuDao apiTokenMenuDao;
    private final RedisHelper redisHelper;

    @PostConstruct
    public void indexApiToken() {
        List<ApiToken> list = apiTokenDao.list();
        for (ApiToken apiToken : list) {
            setApiTokenToUse(apiToken);
        }
    }

    private void setApiTokenToUse(ApiToken apiToken) {
        List<NetUtils.IpMask> ipRules = parseIpMask(apiToken.getIpRule());
        if (apiToken.getExpire() != null) {
            long expire = apiToken.getExpire().getTime() - System.currentTimeMillis();
            if (expire > 0) {
                redisHelper.putExpire(PREFIX + apiToken.getTokenId(), ipRules, expire, TimeUnit.MILLISECONDS);
            }
        } else {
            redisHelper.putValue(PREFIX + apiToken.getTokenId(), ipRules);
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
    public List<ApiTokenVo> listApiToken(){
        List<ApiToken> tokenList = apiTokenDao.listByUserCode(Access.userCode());

        List<ApiTokenVo> list = Collections.copyToList(tokenList, ApiTokenVo.class);
        for(ApiTokenVo tokenVo : list){
            Map<String, Object> accessInfo =
                    redisHelper.getValue("sys-admin:token:api:current:" + tokenVo.getTokenId());
            if(accessInfo != null){
                tokenVo.setAccessIp((String)accessInfo.get("ip"));
                tokenVo.setAccessUrl((String)accessInfo.get("url"));
                tokenVo.setAccessTime((Date)accessInfo.get("time"));
            }
            tokenVo.setMenuIds(apiTokenMenuDao.listMenusByTokenId(tokenVo.getTokenId()));
        }
        return list;
    }

    @Override
    public String creatApiToken(ApiTokenRequest request) {
        // 先保存令牌
        apiTokenDao.save(request);

        // 令牌菜单
        List<String> permits = new ArrayList<>();
        List<Integer> menuIds = request.getMenuIds();
        if(CollectionUtils.isNotEmpty(menuIds)){
            permits = sysMenuDao.queryPermitsByIds(menuIds);
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
                .claim(CLAIM_TYPE, API.val())
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
                .claim(CLAIM_CONFLICT, "N")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, accessProperties.accessSecret());
        if(request.getExpire() != null){
            jwtBuilder.setExpiration(request.getExpire());
        }
        String tokenValue = jwtBuilder.compact();

        // 更新令牌
        request.setTokenValue(tokenValue);
        apiTokenDao.updateById(request);

        // 保存令牌权限
        if(CollectionUtils.isNotEmpty(menuIds)){
            List<ApiTokenMenu> tokenMenus =
                    Collections.copyToList(menuIds, v -> new ApiTokenMenu(request.getTokenId(), v));
            apiTokenMenuDao.saveBatch(tokenMenus);
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
        apiTokenDao.removeById(tokenId);
        apiTokenMenuDao.removeByTokenId(tokenId);
        // 从缓存删除
        redisHelper.delete(PREFIX + tokenId);
    }
}
