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
import com.cowave.sys.admin.infra.auth.dao.LdapConfigDao;
import com.cowave.sys.admin.infra.auth.dao.LdapUserDao;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.LdapUserDtoMapper;
import com.cowave.sys.admin.infra.base.SysOperationHandler;
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
    private final SysRoleDtoMapper sysRoleDtoMapper;
    private final LdapUserDtoMapper ldapUserDtoMapper;
    private final SysOperationHandler sysOperationHandler;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccessUserDetails authenticate(String userAccount, String passWord) {
        LdapConfig ldapConfig = ldapConfigDao.queryByName("cowave");
        if(ldapConfig == null || ldapConfig.getLdapStatus() == 0){
            throw new HttpException(FORBIDDEN, "ldap认证不支持");
        }

        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String filter = "(&(objectClass=" + ldapConfig.getUserClass() + ")(" + ldapConfig.getAccountProperty() + "=" + userAccount + "))";
        boolean isAuthenticated = ldapTemplate.authenticate("", filter, passWord);
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "{frame.auth.failed.passwd}");

        // 首次Ldap认证成功，创建SysUser
        List<LdapUser> list = ldapTemplate.search(
                ldapConfig.getUserDn(), filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(ldapConfig));
        LdapUser newUser = list.get(0);
        LdapUser ldapUser = ldapUserDao.getByAccount(newUser.getUserAccount());
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
            ldapUser.setUserCode(LDAP.generateCode());
            ldapUser.setRoleCode(ldapConfig.getRoleCode());
            ldapUser.setUserPasswd(passWord);
            ldapUser.setLdapName(ldapConfig.getLdapName());
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

        // 登录日志
        OperationInfo operationInfo = new OperationInfo();
        operationInfo.setSuccess(true);
        operationInfo.setOpModule("op_admin");
        operationInfo.setOpType("op_auth");
        operationInfo.setOpAction("op_login");
        operationInfo.setDesc("Ldap登录：" + userAccount);
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
    public LdapConfig queryByName(String ldapName) {
        return ldapConfigDao.queryByName(ldapName);
    }

    @Override
    public void updateConfig(LdapConfig ldapConfig) {
        ldapConfigDao.removeByName(ldapConfig.getLdapName());
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
    public Page<LdapUserDto> listUser(String ldapAccount) {
        return ldapUserDtoMapper.listUser(Access.page(), ldapAccount);
    }

    @Override
    public void deleteUser(Integer userId) {
        ldapUserDao.removeById(userId);
    }

    @Override
    public void changeUserRole(Integer userId, String roleCode) {
        ldapUserDao.updateRoleCodeById(userId, roleCode);
    }
}
