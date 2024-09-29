/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.auth;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.I18Messages;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysMenuDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.FORBIDDEN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ApplicationProperties applicationProperties;
	private final BearerTokenService bearerTokenService;
    private final SysUserDao sysUserDao;
    private final SysDeptDtoMapper sysDeptDtoMapper;
    private final SysRoleDtoMapper sysRoleDtoMapper;
    private final SysMenuDtoMapper sysMenuDtoMapper;

    @Override
	public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        // 用户信息
        SysUser sysUser = sysUserDao.getByUserAccount(userAccount);
        if(sysUser == null){
            throw new UsernameNotFoundException(I18Messages.msg("admin.user.not.exist"));
        }
        HttpAsserts.equals(1, sysUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);

        // 部门信息
        SysDept userDept = sysDeptDtoMapper.getDefaultDeptOfUser(sysUser.getUserId());
        // 角色信息
        List<String> roleCodes = sysRoleDtoMapper.getUserRoles(sysUser.getUserId());
        // 权限信息
        List<String> permitKeys;
        if(roleCodes.contains(Permission.ROLE_ADMIN)){
            permitKeys = List.of(Permission.PERMIT_ADMIN);
        }else{
            permitKeys = sysMenuDtoMapper.getPermitsByUserId(sysUser.getUserId());
        }
        // 构造token
        AccessUserDetails userDetails = sysUser.newUserDetails(userDept);
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        userDetails.setRoles(roleCodes);
        userDetails.setPermissions(permitKeys);
        bearerTokenService.assignAccessRefreshToken(userDetails);
        return userDetails;
	}
}
