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
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.rabc.enums.EnableStatus;
import com.cowave.hub.admin.domain.rabc.HubMenu;
import com.cowave.hub.admin.domain.rabc.dto.HubMenuTree;
import com.cowave.hub.admin.infra.rabc.dao.HubMenuDao;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubMenuDtoMapper;
import com.cowave.hub.admin.service.rabc.HubMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubMenuServiceImpl implements HubMenuService {
	private final HubMenuDao hubMenuDao;
	private final HubMenuDtoMapper hubMenuDtoMapper;

	@Override
	public List<HubMenu> listMenusByAdmin(String tenantId) {
		return hubMenuDao.listMenusByAdmin(tenantId);
	}

	@Override
	public List<HubMenu> listMenusInPublic(String tenantId) {
		return hubMenuDao.listMenusInPublic(tenantId);
	}

	@Override
	public List<HubMenu> listMenusByRoles(String tenantId, List<String> roleList) {
		return hubMenuDtoMapper.listMenusByRoles(tenantId, roleList);
	}

	@Override
	public List<Tree<Integer>> tree(String tenantId) {
		List<HubMenuTree> list = hubMenuDtoMapper.listTree(tenantId);
		return TreeUtil.build(list, 0, DIAGRAM_CONFIG, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
			node.setWeight(menu.getMenuOrder());
			node.put("order", menu.getMenuOrder());
			node.put("menuType", menu.getMenuType());
			node.put("protected", menu.getIsProtected());
			node.put("scopes", menu.getScopes());
		});
	}

	@Override
	public List<HubMenu> list(String menuName, EnableStatus menuStatus) {
		return hubMenuDao.list(menuName, menuStatus);
	}

	@Override
	public List<Tree<Integer>> getApiPermitsByUser(String tenantId) {
		List<HubMenuTree> list = new ArrayList<>();
		// 系统管理员
		if (Access.isAdminUser()) {
			list = hubMenuDtoMapper.listApiPermitsByAdmin(tenantId);
		} else {
			List<String> roleList = Access.userRoles();
			if (!roleList.isEmpty()) {
				list = hubMenuDtoMapper.listApiPermitsByRoles(Access.tenantId(), roleList);
			}
		}
		return TreeUtil.build(list, 0, DIAGRAM_CONFIG, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
			node.put("menuType", menu.getMenuType());
			node.put("scopeId", menu.getScopeId());
			node.put("scopes", menu.getScopes());
		});
	}

	@Override
	public HubMenu info(Integer menuId) {
		return hubMenuDao.getById(menuId);
	}

	@Override
	public void add(HubMenu hubMenu) {
		hubMenuDao.save(hubMenu);
	}

	@Override
	public void delete(Integer menuId) {
		HubMenu preMenu = hubMenuDao.getById(menuId);
		if(preMenu == null) {
			return;
		}
		// 删除当前以及子菜单的角色关联
		hubMenuDtoMapper.loopDeleteMenuRoles(menuId);
		// 删除自己以及子菜单
		hubMenuDtoMapper.loopDeleteMenus(menuId);
	}

	@Override
	public void edit(HubMenu hubMenu) {
		HttpAsserts.notNull(hubMenu.getMenuId(), BAD_REQUEST, "{admin.menu.id.null}");

		HubMenu preMenu = hubMenuDao.getById(hubMenu.getMenuId());
		HttpAsserts.notNull(preMenu, NOT_FOUND, "{admin.menu.not.exist}", hubMenu.getMenuId());

		if(!"C".equals(hubMenu.getMenuType())){
			hubMenu.setComponent(null);
		}
		hubMenuDao.updateMenu(hubMenu);
	}
}
