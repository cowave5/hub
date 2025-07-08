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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.HttpException;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.sys.admin.domain.auth.LdapConfig;
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.domain.auth.dto.LdapUserDto;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.infra.auth.dao.LdapConfigDao;
import com.cowave.sys.admin.infra.auth.dao.LdapUserDao;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.LdapUserDtoMapper;
import com.cowave.sys.admin.infra.base.SysOperationHandler;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.service.auth.LdapAttributesMapper;
import com.cowave.sys.admin.service.auth.LdapService;
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
import static com.cowave.sys.admin.domain.auth.AuthType.LDAP;
import static com.cowave.sys.admin.domain.base.constants.OpAction.LOGIN;
import static com.cowave.sys.admin.domain.base.constants.OpModule.SYSTEM;
import static com.cowave.sys.admin.domain.base.constants.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class LdapServiceImpl implements LdapService {
    private final ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy;
    private final ApplicationProperties applicationProperties;
    private final BearerTokenService bearerTokenService;
    private final LdapUserDao ldapUserDao;
    private final LdapConfigDao ldapConfigDao;
    private final SysTenantDao sysTenantDao;
    private final SysRoleDtoMapper sysRoleDtoMapper;
    private final LdapUserDtoMapper ldapUserDtoMapper;
    private final SysOperationHandler sysOperationHandler;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccessUserDetails authenticate(String tenantId, String userAccount, String passWord) {
        LdapConfig ldapConfig = ldapConfigDao.getById(tenantId);
        if(ldapConfig == null || ldapConfig.getLdapStatus() == 0){
            throw new HttpException(FORBIDDEN, "ldap认证不支持");
        }

        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String filter = "(&(objectClass=" + ldapConfig.getUserClass() + ")(" + ldapConfig.getAccountProperty() + "=" + userAccount + "))";
        boolean isAuthenticated = ldapTemplate.authenticate("", filter, passWord);
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "{frame.auth.pass.invalid}");

        // 首次Ldap认证成功，创建SysUser
        List<LdapUser> list = ldapTemplate.search(
                ldapConfig.getUserDn(), filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(ldapConfig));
        LdapUser newUser = list.get(0);
        LdapUser ldapUser = ldapUserDao.getByAccount(tenantId, newUser.getUserAccount());
        if(ldapUser != null){
            ldapUser.setUserInfo(newUser.getUserInfo());
            ldapUser.setUserPasswd(newUser.getUserPasswd());
            ldapUser.setUserName(newUser.getUserName());
            ldapUser.setUserPhone(newUser.getUserPhone());
            ldapUser.setUserEmail(newUser.getUserEmail());
            ldapUser.setUserPost(newUser.getUserPost());
            ldapUser.setUserDept(newUser.getUserDept());
            ldapUser.setUserLeader(newUser.getUserLeader());
            ldapUser.setUpdateTime(new Date());
            ldapUserDao.updateById(ldapUser);
        }else{
            ldapUser = newUser;
            ldapUser.setUserCode(sysTenantDao.nextUserCode(tenantId, LDAP.val()));
            ldapUser.setRoleCode(ldapConfig.getRoleCode());
            ldapUser.setUserPasswd(passWord);
            ldapUser.setTenantId(ldapConfig.getTenantId());
            ldapUserDao.save(ldapUser);
        }

        String roleCode = ldapUser.getRoleCode();
        List<String> permits;
        if(Permission.ROLE_ADMIN.equals(roleCode)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = sysRoleDtoMapper.getPermitsByRoleCode(roleCode);
        }

        // 构造AccessToken
        AccessUserDetails userDetails = ldapUser.newUserDetails();
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        userDetails.setRoles(List.of(roleCode));
        userDetails.setPermissions(permits);
        bearerTokenService.assignAccessRefreshToken(userDetails);
        // 租户首页
        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        userDetails.setTenantIndex(sysTenant.getViewIndex());

        // 登录日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("Ldap登录：" + userAccount)
                .build();
        sysOperationHandler.create(operationInfo, null);
        return userDetails;
    }

    private LdapTemplate getLdapTemplate(LdapConfig ldapConfig){
        LdapContextSource source = new LdapContextSource();
        dirContextAuthenticationStrategy.ifUnique(source::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        try{
            propertyMapper.from(ldapConfig.getLdapUser()).to(source::setUserDn);
            propertyMapper.from(ldapConfig.getLdapPasswd()).to(source::setPassword);
            propertyMapper.from(ldapConfig.anonymousReadOnly()).to(source::setAnonymousReadOnly);
            propertyMapper.from(ldapConfig.getBaseDn()).to(source::setBase);
            propertyMapper.from(ldapConfig.determineUrls()).to(source::setUrls);
            propertyMapper.from(ldapConfig.getEnvironment()).to(
                    baseEnvironment -> source.setBaseEnvironmentProperties(Collections.unmodifiableMap(baseEnvironment)));
            source.afterPropertiesSet();
        }catch(Exception e){
            throw new HttpException(e, BAD_REQUEST, "{admin.ldap.invalid}");
        }
        return new LdapTemplate(source);
    }

    @Override
    public LdapConfig getConfig(String tenantId) {
        return ldapConfigDao.getById(tenantId);
    }

    @Override
    public void updateConfig(String tenantId, LdapConfig ldapConfig) {
        ldapConfigDao.removeById(ldapConfig.getTenantId());
        ldapConfig.setTenantId(tenantId);
        ldapConfigDao.save(ldapConfig);
    }

    @Override
    public void validConfig(LdapConfig ldapConfig) {
        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String filter = "(&(objectClass=" + ldapConfig.getUserClass() + ")(" + ldapConfig.getAccountProperty() + "=*))";
        List<LdapUser> list;
        try{
            list = ldapTemplate.search(ldapConfig.getUserDn(),
                    filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(ldapConfig));
        }catch(Exception e){
            throw new HttpException(e, BAD_REQUEST, "{admin.ldap.failed.test}");
        }
        if(list.isEmpty()){
            throw new HttpException(BAD_REQUEST, "{admin.ldap.failed.user}");
        }
    }

    @Override
    public Page<LdapUserDto> listUser(String tenantId, String ldapAccount) {
        return ldapUserDtoMapper.listUser(tenantId, ldapAccount, Access.page());
    }

    @Override
    public void deleteUser(String tenantId, Integer userId) {
        ldapUserDao.removeById(tenantId, userId);
    }

    @Override
    public void changeUserRole(String tenantId, Integer userId, String roleCode) {
        ldapUserDao.updateRoleCodeById(tenantId, userId, roleCode);
    }
}
