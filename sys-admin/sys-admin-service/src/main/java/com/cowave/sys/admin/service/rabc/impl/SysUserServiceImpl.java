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
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.vo.UserDiagramNode;
import com.cowave.sys.admin.infra.base.dao.SysConfigDao;
import com.cowave.sys.admin.infra.rabc.dao.*;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserListDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.service.rabc.SysUserService;
import com.cowave.sys.admin.domain.rabc.request.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.sys.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.sys.admin.domain.constants.AuthType.SYS;
import static com.cowave.sys.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
	private final PasswordEncoder passwordEncoder;
	private final SysConfigDao sysConfigDao;
	private final SysTenantDao sysTenantDao;
	private final SysUserDao sysUserDao;
	private final SysUserRoleDao sysUserRoleDao;
	private final SysUserDeptDao sysUserDeptDao;
	private final SysUserTreeDao sysUserTreeDao;
	private final SysUserDtoMapper sysUserDtoMapper;

	@Override
    public Response.Page<UserListDto> list(String tenantId, UserQuery query) {
		if(query.getDeptId() == 0){
			return new Response.Page<>(sysUserDtoMapper.list(tenantId, query), sysUserDtoMapper.count(tenantId, query));
		}else{
			return new Response.Page<>(sysUserDtoMapper.listOfDept(tenantId, query), sysUserDtoMapper.countOfDept(tenantId, query));
		}
    }

	@Override
    public UserInfoDto info(String tenantId, Integer userId) {
        return sysUserDtoMapper.getById(tenantId, userId);
    }

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void create(String tenantId, UserCreate user) {
		String userAccount = user.getUserAccount();
		String userPasswd = user.getUserPasswd();
    	HttpAsserts.notNull(userPasswd, BAD_REQUEST, "{admin.user.passwd.null}");

		long accountCount = sysUserDao.countByAccount(tenantId, userAccount, null);
		HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);

		// 创建用户
		user.setUserType(SYS.getVal());
		user.setUserCode(sysTenantDao.nextUserCode(tenantId, SYS.getVal()));
    	user.setUserPasswd(passwordEncoder.encode(userPasswd));
		sysUserDao.save(user);
    	// 用户角色
		sysUserRoleDao.saveBatch(user.getUserRoles());
		// 上级用户
		sysUserTreeDao.saveBatch(user.getUserParents());
    	// 部门岗位
		sysUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
	@Override
    public void delete(String tenantId, List<Integer> userIds) {
		List<UserInfoDto> list = new ArrayList<>();
		for(Integer userId : userIds){
			UserInfoDto deleteUser = delete(tenantId, userId);
			if(deleteUser != null){
				list.add(deleteUser);
			}
		}
		OperationContext.prepareContent(list);
    }

	private UserInfoDto delete(String tenantId, Integer userId){
		UserInfoDto preUser = sysUserDtoMapper.getById(tenantId, userId);
		if(preUser == null) {
			return null;
		}
		HttpAsserts.notEquals(Access.userAccount(), preUser.getUserAccount(), FORBIDDEN, "{admin.user.forbid.self.delete}");

		// 删除用户
		sysUserDao.removeById(userId);
		// 用户角色
		sysUserRoleDao.removeByUserId(userId);
		// 用户单位
		sysUserDeptDao.removeByUserId(userId);
		// 上级用户
		sysUserTreeDao.removeParentsByUserId(userId);
		// 下级用户
		sysUserTreeDao.removeChildrenByUserId(userId);
		return preUser;
	}

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void edit(String tenantId, UserCreate user) {
		Integer userId = user.getUserId();
		HttpAsserts.notNull(userId, BAD_REQUEST, "{admin.user.id.null}");

		UserInfoDto preUser = sysUserDtoMapper.getById(tenantId, userId);
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", userId);

		// 账号校验
		String userAccount = user.getUserAccount();
		long accountCount = sysUserDao.countByAccount(tenantId, userAccount, userId);
		HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);

		// 操作日志
		OperationContext.prepareContent(preUser);

		// 更新用户
		sysUserDao.updateUser(user);
    	// 用户角色
		sysUserRoleDao.removeByUserId(userId);
		sysUserRoleDao.saveBatch(user.getUserRoles());
		// 用户关系
		sysUserTreeDao.removeParentsByUserId(userId);
		// 检测环，children与parents不能有交集
		List<Integer> parentIds = user.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)){
			List<Integer> childIds = sysUserDtoMapper.childIds(userId);
			childIds.add(userId);
			HttpAsserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
			sysUserTreeDao.saveBatch(user.getUserParents());
		}
    	// 部门岗位
		sysUserDeptDao.removeByUserId(userId);
		sysUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

	@Override
	public void changeRoles(String tenantId, UserRoleUpdate user) {
		UserInfoDto preUser = sysUserDtoMapper.getById(tenantId, user.getUserId());
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
		sysUserRoleDao.removeByUserId(user.getUserId());
		sysUserRoleDao.saveBatch(user.getUserRoles());
	}

	@Override
	public void changeStatus(String tenantId, UserStatusUpdate user) {
		UserInfoDto preUser = sysUserDtoMapper.getById(tenantId, user.getUserId());
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
		sysUserDao.updateStatusById(user.getUserId(), user.getUserStatus());
	}

	@Override
	public void changePasswd(String tenantId, UserPasswdUpdate user) {
		UserInfoDto preUser = sysUserDtoMapper.getById(tenantId, user.getUserId());
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
		String newPasswd = passwordEncoder.encode(user.getUserPasswd());
		sysUserDao.updatePasswdById(user.getUserId(), newPasswd);
	}

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
	@Override
	public void importUsers(String tenantId, List<SysUser> list, boolean overwrite) {
		String passwd = sysConfigDao.getConfigValue(tenantId, "sys.initPassword");
		for(SysUser sysUser : list){
			sysUser.setTenantId(tenantId);
			sysUser.setUserType(SYS.getVal());
			sysUser.setUserCode(sysTenantDao.nextUserCode(tenantId, SYS.getVal()));
			sysUser.setUserPasswd(passwordEncoder.encode(passwd));
			sysUser.setCreateBy(Access.userCode());
			sysUser.setCreateTime(Access.accessTime());
			sysUser.setUpdateBy(Access.userCode());
			sysUser.setUpdateTime(Access.userCode());
		}
		sysUserDtoMapper.batchInsert(list, overwrite);
	}

	@Override
	public List<SysUser> listForExport(String tenantId, UserExportQuery userExport) {
		return sysUserDao.list(tenantId, userExport);
	}

	@Cacheable(value = USER_DIAGRAM, key = "#tenantId")
	@Override
	public Tree<Integer> getDiagram(String tenantId) {
		List<UserDiagramNode> list = sysUserDtoMapper.listDiagramNodes(tenantId);
		// 根节点
        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        list.add(UserDiagramNode.newRootNode(sysTenant.getTenantName()));
		// 构造Tree
		return TreeUtil.build(list, -1, DIAGRAM_CONFIG, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
            node.put("rank", u.getUserRank());
        }).get(0);
	}

	@Override
	public List<UserNameDto> getUserCandidates(String tenantId, Integer userId) {
		if(userId == null){
			userId = Access.userId();
		}
		return sysUserDtoMapper.getUserCandidates(tenantId, userId);
	}

	@Override
	public List<String> getNamesById(String tenantId, List<Integer> userIds) {
		return sysUserDao.getNamesById(tenantId, userIds);
	}

	@Override
	public Page<SysUser> getUserOptions(String tenantId, UserMemberQuery query) {
		return sysUserDao.getUserOptions(tenantId, query);
	}
}
