/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.auth;

import java.util.List;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.filter.security.Permission;
import com.cowave.commons.framework.filter.security.TokenService;
import com.cowave.commons.framework.helper.operation.OperationAccepter;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.model.admin.SysLog;
import com.cowave.sys.model.admin.SysUserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.api.mapper.SysUserMapper;
import com.cowave.commons.framework.configuration.ClusterInfo;
import com.cowave.sys.model.admin.SysUser;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final TokenService tokenService;

    private final SysUserMapper sysUserMapper;

    private final ClusterInfo clusterInfo;

    private final OperationAccepter<SysLog> operationAccepter;

    @Override
	public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
    	SysUser sysUser = sysUserMapper.queryByAccount(userAccount);
        if(sysUser == null){
            throw new UserNotFoundException("user.notexist");
        }
        Asserts.equals(1, sysUser.getUserStatus(), "auth.account.disable", userAccount);

        List<String> roleCodes = sysUser.getRoleList().stream().map(SysUserRole::getRoleCode).toList();
        List<String> permits;
        if(roleCodes.contains(Permission.ROLE_ADMIN)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = sysUserMapper.permitKeys(sysUser.getUserId());
        }

        AccessToken accessToken = SysUser.accessToken(sysUser);
        accessToken.setRoles(roleCodes);
        accessToken.setPermissions(permits);
        accessToken.setClusterId(clusterInfo.getId());
        accessToken.setClusterName(clusterInfo.getName());
        accessToken.setClusterLevel(clusterInfo.getLevel());
        tokenService.assignToken(accessToken);

        SysLog sysLog = new SysLog();
        sysLog.initialize();
        sysLog.recordOperation("admin_login", "login", "登录");
        operationAccepter.accept(sysLog);
        return accessToken;
	}
}
