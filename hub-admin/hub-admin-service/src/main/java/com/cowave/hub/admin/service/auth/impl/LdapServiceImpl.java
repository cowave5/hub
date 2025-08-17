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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.HttpException;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.rabc.*;
import com.cowave.hub.admin.infra.rabc.dao.*;
import com.cowave.hub.admin.domain.auth.HubLdap;
import com.cowave.hub.admin.domain.auth.HubLdapUser;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.infra.auth.dao.HubLdapDao;
import com.cowave.hub.admin.infra.auth.dao.HubLdapUserDao;
import com.cowave.hub.admin.infra.auth.dao.UserDetailsDao;
import com.cowave.hub.admin.infra.AdminOperationHandler;
import com.cowave.hub.admin.service.auth.LdapAttributesMapper;
import com.cowave.hub.admin.service.auth.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.SearchControls;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class LdapServiceImpl implements LdapService {
    private final ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy;
    private final BearerTokenService bearerTokenService;
    private final RedisHelper redisHelper;
    private final HubLdapDao hubLdapDao;
    private final HubLdapUserDao hubLdapUserDao;
    private final HubTenantDao hubTenantDao;
    private final AdminOperationHandler operationHandler;
    private final HubUserDao hubUserDao;
    private final HubUserDiagramDao hubUserDiagramDao;
    private final HubRoleDao hubRoleDao;
    private final HubUserRoleDao hubUserRoleDao;
    private final UserDetailsDao userDetailsDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccessUserDetails authenticate(String tenantId, String userAccount, String passWord) {
        HubLdap hubLdap = hubLdapDao.getById(tenantId);
        if(hubLdap == null || hubLdap.getLdapStatus() == 0){
            throw new HttpException(FORBIDDEN, "ldap认证不支持");
        }

        LdapTemplate ldapTemplate = getLdapTemplate(hubLdap);
        String filter = "(&(objectClass=" + hubLdap.getUserClass() + ")(" + hubLdap.getAccountProperty() + "=" + userAccount + "))";
        boolean isAuthenticated = ldapTemplate.authenticate("", filter, passWord);
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "{frame.auth.pass.invalid}");

        // Ldap用户信息
        List<HubLdapUser> list = ldapTemplate.search(
                hubLdap.getUserDn(), filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(hubLdap));
        HubLdapUser newUser = list.get(0);
        HubLdapUser hubLdapUser = hubLdapUserDao.getByAccount(tenantId, newUser.getUserAccount());
        if(hubLdapUser != null){
            hubLdapUser.setUserInfo(newUser.getUserInfo());
            hubLdapUser.setUserPasswd(newUser.getUserPasswd());
            hubLdapUser.setUserName(newUser.getUserName());
            hubLdapUser.setUserPhone(newUser.getUserPhone());
            hubLdapUser.setUserEmail(newUser.getUserEmail());
            hubLdapUser.setUserPost(newUser.getUserPost());
            hubLdapUser.setUserDept(newUser.getUserDept());
            hubLdapUser.setUserLeader(newUser.getUserLeader());
            hubLdapUser.setUpdateTime(new Date());
            hubLdapUserDao.updateById(hubLdapUser);
        }else{
            hubLdapUser = newUser;
            hubLdapUser.setUserPasswd(passWord);
            hubLdapUser.setTenantId(hubLdap.getTenantId());
            hubLdapUserDao.save(hubLdapUser);
        }

        // 对应系统用户
        String userCode = UserType.LDAP.newCode(tenantId, newUser.getUserAccount());
        HubUser hubUser = hubUserDao.getByCode(userCode);
        if(hubUser == null){
            hubUser = new HubUser();
            hubUser.setUserCode(userCode);
            hubUser.setTenantId(hubLdap.getTenantId());
            hubUser.setUserType(UserType.LDAP);
            hubUser.setUserAccount(newUser.getUserAccount());
            hubUser.setUserName(newUser.getUserName());
            hubUser.setUserPhone(newUser.getUserPhone());
            hubUser.setUserEmail(newUser.getUserEmail());
            hubUserDao.save(hubUser);
            // role
            HubRole hubRole = hubRoleDao.getByCode(tenantId, hubLdap.getRoleCode());
            if(hubRole != null) {
                HubUserRole userRole = new HubUserRole(hubUser.getUserId(), hubRole.getRoleId());
                hubUserRoleDao.save(userRole);
            }
            // 用户关系
            HubUserDiagram userDiagram = new HubUserDiagram(hubUser.getUserId(), 0, hubLdap.getTenantId());
            hubUserDiagramDao.save(userDiagram);
            // 用户树缓存
            redisHelper.delete(USER_DIAGRAM + ":" + tenantId);
            redisHelper.delete(DEPT_USER_DIAGRAM + ":" + tenantId);
        }else{
            hubUser.setUserName(newUser.getUserName());
            hubUser.setUserPhone(newUser.getUserPhone());
            hubUser.setUserEmail(newUser.getUserEmail());
            hubUserDao.updateLdapByCode(hubUser);
        }

        // 创建令牌
        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        AccessUserDetails userDetails = userDetailsDao.newUserDetails(UserType.LDAP.getVal(), hubTenant, hubUser);
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 登录日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("Ldap登录：" + userAccount)
                .build();
        operationHandler.create(operationInfo, null);
        return userDetails;
    }

    private LdapTemplate getLdapTemplate(HubLdap hubLdap){
        LdapContextSource source = new LdapContextSource();
        dirContextAuthenticationStrategy.ifUnique(source::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        try{
            propertyMapper.from(hubLdap.getLdapUser()).to(source::setUserDn);
            propertyMapper.from(hubLdap.getLdapPasswd()).to(source::setPassword);
            propertyMapper.from(hubLdap.anonymousReadOnly()).to(source::setAnonymousReadOnly);
            propertyMapper.from(hubLdap.getBaseDn()).to(source::setBase);
            propertyMapper.from(hubLdap.determineUrls()).to(source::setUrls);
            propertyMapper.from(hubLdap.getEnvironment()).to(
                    baseEnvironment -> source.setBaseEnvironmentProperties(Collections.unmodifiableMap(baseEnvironment)));
            source.afterPropertiesSet();
        }catch(Exception e){
            throw new HttpException(e, BAD_REQUEST, "{admin.ldap.invalid}");
        }
        return new LdapTemplate(source);
    }

    @Override
    public HubLdap getLdap(String tenantId) {
        return hubLdapDao.getById(tenantId);
    }

    @Override
    public void editLdap(String tenantId, HubLdap hubLdap) {
        hubLdapDao.removeById(hubLdap.getTenantId());
        hubLdap.setTenantId(tenantId);
        hubLdapDao.save(hubLdap);
    }

    @Override
    public void validConfig(HubLdap hubLdap) {
        LdapTemplate ldapTemplate = getLdapTemplate(hubLdap);
        String filter = "(&(objectClass=" + hubLdap.getUserClass() + ")(" + hubLdap.getAccountProperty() + "=*))";
        List<HubLdapUser> list;
        try{
            list = ldapTemplate.search(hubLdap.getUserDn(),
                    filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(hubLdap));
        }catch(Exception e){
            throw new HttpException(e, BAD_REQUEST, "{admin.ldap.failed.test}");
        }
        if(list.isEmpty()){
            throw new HttpException(BAD_REQUEST, "{admin.ldap.failed.user}");
        }
    }

    @Override
    public Page<HubLdapUser> listUser(String tenantId, String ldapAccount) {
        return hubLdapUserDao.getUserPage(tenantId, ldapAccount);
    }
}
