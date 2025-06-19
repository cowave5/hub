/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc.impl;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDeptDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserRoleDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserTreeDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserListDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.infra.base.redis.SysConfigRedis;
import com.cowave.sys.admin.infra.rabc.redis.SysDeptRedis;
import com.cowave.sys.admin.infra.rabc.redis.SysUserRedis;
import com.cowave.sys.admin.service.rabc.SysUserService;
import com.cowave.sys.admin.domain.rabc.request.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.auth.AccessType.SYS;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
	private final PasswordEncoder passwordEncoder;
	private final SysDeptRedis sysDeptRedis;
	private final SysUserRedis sysUserRedis;
	private final SysConfigRedis sysConfigRedis;
	private final SysUserDao sysUserDao;
	private final SysUserRoleDao sysUserRoleDao;
	private final SysUserDeptDao sysUserDeptDao;
	private final SysUserTreeDao sysUserTreeDao;
	private final SysUserDtoMapper sysUserDtoMapper;

	@Override
    public Response.Page<UserListDto> list(UserQuery query) {
		if(query.getDeptId() == null){
			return new Response.Page<>(sysUserDtoMapper.list(query), sysUserDtoMapper.count(query));
		}else{
			return new Response.Page<>(sysUserDtoMapper.listOfDept(query), sysUserDtoMapper.countOfDept(query));
		}
    }

	@Override
    public UserInfoDto info(Integer userId) {
        return sysUserDtoMapper.getById(userId);
    }

    @Override
    public void create(UserCreate user) {
		String userAccount = user.getUserAccount();
		String userPasswd = user.getUserPasswd();
    	HttpAsserts.notNull(userPasswd, BAD_REQUEST, "{admin.user.passwd.notnull}");

		long accountCount = sysUserDao.countUserAccount(userAccount, null);
		HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);
		// 创建用户
		user.setUserCode(SYS.generateCode());
    	user.setUserPasswd(passwordEncoder.encode(userPasswd));
		sysUserDao.save(user);
    	// 用户角色
		sysUserRoleDao.saveBatch(user.getUserRoles());
		// 上级用户
		sysUserTreeDao.saveBatch(user.getUserParents());
    	// 部门岗位
		sysUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

	@Override
    public void delete(List<Integer> userIds) {
		List<UserInfoDto> list = new ArrayList<>();
		for(Integer userId : userIds){
			UserInfoDto deleteUser = delete(userId);
			if(deleteUser != null){
				list.add(deleteUser);
			}
		}
		OperationContext.prepareContent(list);
    }

	private UserInfoDto delete(Integer userId){
		UserInfoDto preUser = sysUserDtoMapper.getById(userId);
		if(preUser == null) {
			return null;
		}
		HttpAsserts.notEquals(Access.userAccount(), preUser.getUserAccount(), FORBIDDEN, "{admin.user.forbid.self.delete}");
		// 删除用户
		sysUserDao.removeById(userId);
		// 用户角色
		sysUserRoleDao.deleteByUserId(userId);
		// 用户单位
		sysUserDeptDao.clearByUserId(userId);
		// 上级用户
		sysUserTreeDao.deleteParentsByUserId(userId);
		// 下级用户
		sysUserTreeDao.deleteChildrenByUserId(userId);
		return preUser;
	}

    @Override
    public void edit(UserCreate user) {
		Integer userId = user.getUserId();
		HttpAsserts.notNull(userId, BAD_REQUEST, "{admin.user.id.notnull}");
		// 账号校验
		String userAccount = user.getUserAccount();
		long accountCount = sysUserDao.countUserAccount(userAccount, userId);
		HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);
		// 操作日志
		UserInfoDto preUser = getAndCheckEditable(userId);
		OperationContext.prepareContent(preUser);
		// 更新用户
		sysUserDao.updateUser(user);
    	// 用户角色
		sysUserRoleDao.deleteByUserId(userId);
		sysUserRoleDao.saveBatch(user.getUserRoles());
		// 上级用户
		sysUserTreeDao.deleteParentsByUserId(userId);
		List<Integer> parentIds = user.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)){
			// 检测循环，children与parents不能有交集
			List<Integer> childIds = sysUserDtoMapper.childIds(userId);
			childIds.add(userId);
			HttpAsserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
			sysUserTreeDao.saveBatch(user.getUserParents());
		}
    	// 部门岗位
		sysUserDeptDao.clearByUserId(userId);
		sysUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

	private UserInfoDto getAndCheckEditable(Integer userId){
		UserInfoDto preUser = sysUserDtoMapper.getById(userId);
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", userId);
		return preUser;
	}

	@Override
	public void changeRoles(UserRoleUpdate user) {
		getAndCheckEditable(user.getUserId());
		sysUserRoleDao.deleteByUserId(user.getUserId());
		sysUserRoleDao.saveBatch(user.getUserRoles());
	}

	@Override
	public void changeStatus(UserStatusUpdate user) {
		getAndCheckEditable(user.getUserId());
		sysUserDao.updateStatusById(user.getUserId(), user.getUserStatus());
	}

	@Override
	public void changePasswd(UserPasswdUpdate user) {
		getAndCheckEditable(user.getUserId());
		String newPasswd = passwordEncoder.encode(user.getUserPasswd());
		sysUserDao.updatePasswdById(user.getUserId(), newPasswd);
	}

	@Override
	public void importUsers(List<SysUser> list, boolean overwrite) {
		String passwd = sysConfigRedis.getConfigValue("sys.user.initPassword");
		for(SysUser sysUser : list){
			sysUser.setUserCode(SYS.generateCode());
			sysUser.setUserPasswd(passwordEncoder.encode(passwd));
			sysUser.setCreateBy(Access.userCode());
			sysUser.setCreateTime(Access.accessTime());
		}
		sysUserDtoMapper.batchInsert(list, overwrite);
	}

	@Override
	public List<SysUser> listForExport(UserExportQuery userExport) {
		return sysUserDao.listForExport(userExport);
	}

	@Override
	public Tree<String> getDiagram() {
		return sysUserRedis.getDiagram();
	}

	@Override
	public void refreshDiagram() {
		sysUserRedis.refreshDiagram();
		sysDeptRedis.refreshUserDiagram();
	}

	@Override
	public List<UserNameDto> getUserCandidates(Integer userId) {
		if(userId == null){
			userId = Access.userId();
		}
		return sysUserDtoMapper.getUserCandidates(userId);
	}

	@Override
	public List<String> getNamesById(List<Integer> userIds) {
		return sysUserDao.getNamesById(userIds);
	}
}
