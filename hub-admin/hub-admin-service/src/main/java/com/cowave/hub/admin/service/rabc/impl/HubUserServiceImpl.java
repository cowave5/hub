/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.rabc.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.hub.admin.infra.rabc.dao.*;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.vo.UserDiagramNode;
import com.cowave.hub.admin.infra.sys.dao.HubConfigDao;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubUserDtoMapper;
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.UserListDto;
import com.cowave.hub.admin.domain.rabc.dto.UserNameDto;
import com.cowave.hub.admin.service.rabc.HubUserService;
import com.cowave.hub.admin.domain.rabc.request.*;
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
import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.hub.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubUserServiceImpl implements HubUserService {
	private final BearerTokenService bearerTokenService;
	private final PasswordEncoder passwordEncoder;
	private final HubConfigDao hubConfigDao;
	private final HubTenantDao hubTenantDao;
	private final HubUserDao hubUserDao;
	private final HubUserRoleDao hubUserRoleDao;
	private final HubUserDeptDao hubUserDeptDao;
	private final HubUserDiagramDao hubUserDiagramDao;
	private final HubUserDtoMapper hubUserDtoMapper;

	@Override
    public Response.Page<UserListDto> list(String tenantId, UserQuery query) {
		if(query.getDeptId() == 0){
			return new Response.Page<>(hubUserDtoMapper.list(tenantId, query), hubUserDtoMapper.count(tenantId, query));
		}else{
			return new Response.Page<>(hubUserDtoMapper.listOfDept(tenantId, query), hubUserDtoMapper.countOfDept(tenantId, query));
		}
    }

	@Override
    public UserInfoDto info(String tenantId, Integer userId) {
        return hubUserDtoMapper.getById(tenantId, userId);
    }

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void create(String tenantId, UserCreate user) {
		String userAccount = user.getUserAccount();
		String userPasswd = user.getUserPasswd();
    	HttpAsserts.notNull(userPasswd, BAD_REQUEST, "{admin.user.passwd.null}");

		long accountCount = hubUserDao.countByAccount(tenantId, UserType.SYS, userAccount, null);
		HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);

		// 创建用户
		user.setUserType(UserType.SYS);
		user.setUserCode(UserType.SYS.newCode(tenantId, userAccount));
    	user.setUserPasswd(passwordEncoder.encode(userPasswd));
		hubUserDao.save(user);
    	// 用户角色
		hubUserRoleDao.saveBatch(user.getUserRoles());
		// 上级用户
		hubUserDiagramDao.saveBatch(user.getUserParents());
    	// 部门岗位
		hubUserDeptDao.saveBatch(user.getUserDeptPosts());
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
		UserInfoDto preUser = hubUserDtoMapper.getById(tenantId, userId);
		if(preUser == null) {
			return null;
		}
		HttpAsserts.notEquals(Access.userAccount(), preUser.getUserAccount(), FORBIDDEN, "{admin.user.forbid.self.delete}");

		// 删除用户
		hubUserDao.removeById(userId);
		// 用户角色
		hubUserRoleDao.removeByUserId(userId);
		// 用户单位
		hubUserDeptDao.removeByUserId(userId);
		// 上级用户
		hubUserDiagramDao.removeParentsByUserId(userId);
		// 下级用户
		hubUserDiagramDao.removeChildrenByUserId(userId);
		// 强制退出
		bearerTokenService.revokeRefreshToken(tenantId, preUser.getUserType().getVal(), preUser.getUserAccount());
		return preUser;
	}

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void edit(String tenantId, UserCreate user) {
		Integer userId = user.getUserId();
		HttpAsserts.notNull(userId, BAD_REQUEST, "{admin.user.id.null}");

		UserInfoDto preUser = hubUserDtoMapper.getById(tenantId, userId);
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", userId);

		// 账号校验
		String userAccount = user.getUserAccount();
		long accountCount = hubUserDao.countByAccount(tenantId, UserType.SYS, userAccount, userId);
		HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);

		// 操作日志
		OperationContext.prepareContent(preUser);

		// 更新用户
		hubUserDao.updateUser(user);
    	// 用户角色
		hubUserRoleDao.removeByUserId(userId);
		hubUserRoleDao.saveBatch(user.getUserRoles());
		// 用户关系
		hubUserDiagramDao.removeParentsByUserId(userId);
		// 检测环，children与parents不能有交集
		List<Integer> parentIds = user.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)){
			List<Integer> childIds = hubUserDtoMapper.childIds(userId);
			childIds.add(userId);
			HttpAsserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
			hubUserDiagramDao.saveBatch(user.getUserParents());
		}
    	// 部门岗位
		hubUserDeptDao.removeByUserId(userId);
		hubUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

	@Override
	public void changeRoles(String tenantId, UserRoleUpdate user) {
		UserInfoDto preUser = hubUserDtoMapper.getById(tenantId, user.getUserId());
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
		hubUserRoleDao.removeByUserId(user.getUserId());
		hubUserRoleDao.saveBatch(user.getUserRoles());
	}

	@Override
	public void changeStatus(String tenantId, UserStatusUpdate user) {
		UserInfoDto preUser = hubUserDtoMapper.getById(tenantId, user.getUserId());
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
		hubUserDao.updateStatusById(user.getUserId(), user.getUserStatus());
	}

	@Override
	public void changePasswd(String tenantId, UserPasswdUpdate user) {
		UserInfoDto preUser = hubUserDtoMapper.getById(tenantId, user.getUserId());
		HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
		String newPasswd = passwordEncoder.encode(user.getUserPasswd());
		hubUserDao.updatePasswdById(user.getUserId(), newPasswd);
	}

	@CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
	@Override
	public void importUsers(String tenantId, List<HubUser> list, boolean overwrite) {
		String passwd = hubConfigDao.getConfigValue(tenantId, "hub.initPassword");
		for(HubUser hubUser : list){
			hubUser.setTenantId(tenantId);
			hubUser.setUserType(UserType.SYS);
			hubUser.setUserCode(UserType.SYS.newCode(tenantId, hubUser.getUserAccount()));
			hubUser.setUserPasswd(passwordEncoder.encode(passwd));
			hubUser.setCreateBy(Access.userCode());
			hubUser.setCreateTime(Access.accessTime());
			hubUser.setUpdateBy(Access.userCode());
			hubUser.setUpdateTime(Access.userCode());
		}
		hubUserDtoMapper.batchInsert(list, overwrite);
	}

	@Override
	public List<HubUser> listForExport(String tenantId, UserExportQuery userExport) {
		return hubUserDao.list(tenantId, userExport);
	}

	@Cacheable(value = USER_DIAGRAM, key = "#tenantId")
	@Override
	public Tree<Integer> getDiagram(String tenantId) {
		List<UserDiagramNode> list = hubUserDtoMapper.listDiagramNodes(tenantId);
		// 根节点
        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        list.add(UserDiagramNode.newRootNode(hubTenant.getTenantName()));
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
		return hubUserDtoMapper.getUserCandidates(tenantId, userId);
	}

	@Override
	public List<String> getNamesById(String tenantId, List<Integer> userIds) {
		return hubUserDao.getNamesById(tenantId, userIds);
	}

	@Override
	public Page<HubUser> getUserOptions(String tenantId, UserMemberQuery query) {
		return hubUserDao.getUserOptions(tenantId, query);
	}
}
