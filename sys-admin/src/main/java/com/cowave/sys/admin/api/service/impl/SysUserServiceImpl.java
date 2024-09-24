/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.*;

import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.caches.SysConfigCaches;
import com.cowave.sys.admin.api.service.SysUserService;
import com.cowave.sys.model.admin.SysUserDept;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cowave.sys.admin.api.mapper.SysUserMapper;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.model.admin.SysUser;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl implements SysUserService {

	private final SysConfigCaches sysConfigCaches;

	private final SysUserMapper sysUserMapper;

	private final BCryptPasswordEncoder bcryptPasswordEncoder;

	@Override
    public List<SysUser> list(SysUser sysUser) {
		if(sysUser.getDeptId() == null){
			return sysUserMapper.list(sysUser);
		}else{
			return sysUserMapper.listWithDept(sysUser);
		}
    }

	@Override
	public int count(SysUser sysUser) {
		if(sysUser.getDeptId() == null){
			return sysUserMapper.count(sysUser);
		}else{
			return sysUserMapper.countWithDept(sysUser);
		}
	}

	@Override
    public SysUser info(Long userId) {
        return sysUserMapper.info(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysUser sysUser) {
    	Asserts.notNull(sysUser.getUserPasswd(), "user.notnull.passwd");
    	sysUser.setUserPasswd(bcryptPasswordEncoder.encode(sysUser.getUserPasswd()));
    	// 新增用户
    	sysUserMapper.insert(sysUser);
    	Asserts.notNull(sysUser.getUserId(), "user.conflict.account", sysUser.getUserAccount());
    	// 用户角色
		inputUserRoles(sysUser, false);
    	// 部门岗位
		inputUserDepts(sysUser, false);
		// 上级用户
		inputUserParents(sysUser, false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUser edit(SysUser sysUser) {
		SysUser preUser = assertEditable(sysUser.getUserId());
    	int accountsCount = sysUserMapper.countAccounts(sysUser.getUserId(), sysUser.getUserAccount());
    	Asserts.equals(0, accountsCount, "user.conflict.account", sysUser.getUserAccount());
    	// 更新用户
    	sysUserMapper.update(sysUser);
    	// 用户角色
		inputUserRoles(sysUser, true);
    	// 部门岗位
		inputUserDepts(sysUser, true);
		// 上级用户
		inputUserParents(sysUser, true);
    	return preUser;
    }

	private SysUser assertEditable(Long userId){
		Asserts.notNull(userId, "user.notnull.id");
		SysUser preUser = sysUserMapper.info(userId);
		Asserts.notNull(preUser, "user.notexist", userId);
		Asserts.notEquals(1, preUser.getReadOnly(), "user.forbid.edit.readonly", preUser.getUserAccount());
		return preUser;
	}

	private void inputUserRoles(SysUser sysUser, boolean overwrite){
		if(overwrite){
			sysUserMapper.deleteUserRoles(sysUser.getUserId());
		}
		if(CollectionUtils.isNotEmpty(sysUser.getRoleIds())) {
			sysUserMapper.insertUserRoles(sysUser.getUserId(), sysUser.getRoleIds());
		}
	}

	private void inputUserDepts(SysUser sysUser, boolean overwrite){
		if(overwrite){
			sysUserMapper.deleteUserDepts(sysUser.getUserId());
		}
		if(CollectionUtils.isNotEmpty(sysUser.getDeptPostIds())) {
			List<SysUserDept> deptList = new ArrayList<>();
			for(String deptPostId : sysUser.getDeptPostIds()){
				deptList.add(SysUserDept.parseDeptPost(deptPostId, sysUser.getUserId()));
			}
			sysUserMapper.insertUserDepts(deptList);
		}
	}

	private void inputUserParents(SysUser sysUser, boolean overwrite){
		if(overwrite){
			sysUserMapper.deleteUserParents(sysUser.getUserId());
		}
		List<Long> parentIds = sysUser.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)) {
			if(overwrite){
				List<Long> childIds = sysUserMapper.childIds(sysUser.getUserId());
				childIds.add(sysUser.getUserId());
				Asserts.isTrue(Collections.disjoint(childIds, parentIds), "user.tree.cycle");
			}
			sysUserMapper.insertUserParents(sysUser.getUserId(), parentIds);
		}
	}

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysUser> delete(Long[] userIds) {
		List<SysUser> list = new ArrayList<>(userIds.length);
		for(Long userId : userIds){
			SysUser deleteUser = delete(userId);
			if(deleteUser != null){
				list.add(deleteUser);
			}
		}
		return list;
    }

	private SysUser delete(Long userId){
		SysUser preUser = sysUserMapper.info(userId);
		if(preUser == null) {
			return null;
		}
		Asserts.notEquals(Access.userAccount(), preUser.getUserAccount(), "user.forbid.delete.self");
		Asserts.notEquals(1, preUser.getReadOnly(), "user.forbid.delete.readonly");
		// 删除用户
		sysUserMapper.delete(userId);
		// 用户角色
		sysUserMapper.deleteUserRoles(userId);
		// 用户单位
		sysUserMapper.deleteUserDepts(userId);
		// 上级用户
		sysUserMapper.deleteUserParents(userId);
		// 下级用户
		sysUserMapper.deleteUserChilds(userId);
		return preUser;
	}

	@Override
	public void changeStatus(SysUser sysUser) {
		assertEditable(sysUser.getUserId());
		sysUserMapper.changeStatus(sysUser);
	}

	@Override
	public void changePasswd(SysUser sysUser) {
		assertEditable(sysUser.getUserId());
		Asserts.notNull(sysUser.getUserPasswd(), "user.notnull.passwd");
		sysUser.setUserPasswd(bcryptPasswordEncoder.encode(sysUser.getUserPasswd()));
		sysUserMapper.changePasswd(sysUser);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void changeRoles(SysUser sysUser) {
		assertEditable(sysUser.getUserId());
		inputUserRoles(sysUser, true);
	}

	@Override
	public void changeReadonly(SysUser sysUser) {
		sysUserMapper.changeReadonly(sysUser);
	}

	@Override
	public List<SysUser> leaders(Long userId) {
		if(userId == null){
			userId = Access.userId();
		}
		return sysUserMapper.leaders(userId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void importExcelData(List<SysUser> list, boolean overwrite) {
		String passwd = sysConfigCaches.getValue("sys.user.initPassword");
		for (SysUser sysUser : list) {
			sysUser.setUserPasswd(bcryptPasswordEncoder.encode(passwd));
		}
		sysUserMapper.batchInsert(list, overwrite);
	}
}
