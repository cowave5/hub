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
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.infra.rabc.dao.SysMenuDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysMenuDtoMapper;
import com.cowave.sys.admin.service.rabc.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl implements SysMenuService {
	private final SysMenuDao sysMenuDao;
	private final SysMenuDtoMapper sysMenuDtoMapper;

	@Override
	public List<SysMenu> listMenusByAdmin(String tenantId) {
		return sysMenuDao.listMenusByAdmin(tenantId);
	}

	@Override
	public List<SysMenu> listMenusByRoles(String tenantId, List<String> roleList) {
		return sysMenuDtoMapper.listMenusByRoles(tenantId, roleList);
	}

	@Override
	public List<Tree<Integer>> tree(String tenantId) {
		List<SysMenu> list = sysMenuDao.listTree(tenantId);
		List<Tree<Integer>> treeList = TreeUtil.build(list, 0, DIAGRAM_CONFIG, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
			node.put("menuType", menu.getMenuType());
			node.put("order", menu.getMenuOrder());
		});
		treeList.sort(Comparator.comparingInt(node -> (Integer) node.get("order")));
		return treeList;
	}

	@Override
	public List<SysMenu> list(String menuName, Integer menuStatus) {
		return sysMenuDao.list(menuName, menuStatus);
	}

	@Override
	public List<Tree<Integer>> getApiPermitsByUser(String tenantId) {
		List<SysMenu> list = new ArrayList<>();
		// 系统管理员
		if (Access.isAdminUser()) {
			list = sysMenuDao.listApiPermitsByAdmin(tenantId);
		} else {
			List<String> roleList = Access.userRoles();
			if (!roleList.isEmpty()) {
				list = sysMenuDtoMapper.listApiPermitsByRoles(roleList);
			}
		}
		return TreeUtil.build(list, 0, DIAGRAM_CONFIG, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
			node.put("menuType", menu.getMenuType());
		});
	}

	@Override
	public SysMenu info(Integer menuId) {
		return sysMenuDao.getById(menuId);
	}

	@Override
	public void add(SysMenu sysMenu) {
		sysMenuDao.save(sysMenu);
	}

	@Override
	public void delete(Integer menuId) {
		SysMenu preMenu = sysMenuDao.getById(menuId);
		if(preMenu == null) {
			return;
		}
		// 删除当前以及子菜单的角色关联
		sysMenuDtoMapper.loopDeleteMenuRoles(menuId);
		// 删除自己以及子菜单
		sysMenuDtoMapper.loopDeleteMenus(menuId);
	}

	@Override
	public void edit(SysMenu sysMenu) {
		HttpAsserts.notNull(sysMenu.getMenuId(), BAD_REQUEST, "{admin.menu.id.notnull}");

		SysMenu preMenu = sysMenuDao.getById(sysMenu.getMenuId());
		HttpAsserts.notNull(preMenu, NOT_FOUND, "{admin.menu.not.exist}", sysMenu.getMenuId());

		if(!"C".equals(sysMenu.getMenuType())){
			sysMenu.setComponent(null);
		}
		sysMenuDao.updateMenu(sysMenu);
	}
}
