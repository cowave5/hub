/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shanhuiming
 */
@Repository
public class SysMenuDao extends ServiceImpl<SysMenuMapper, SysMenu> {

    /**
     * 菜单权限（管理员）
     */
    public List<SysMenu> listMenusByAdmin(String tenantId) {
        return lambdaQuery()
                .eq(SysMenu::getIsVisible, 1)
                .eq(SysMenu::getMenuStatus, 1)
                .in(SysMenu::getTenantId, List.of("#", tenantId))
                .in(SysMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    /**
     * 菜单权限（公共菜单）
     */
    public List<SysMenu> listMenusInPublic(String tenantId) {
        return lambdaQuery()
                .eq(SysMenu::getIsVisible, 1)
                .eq(SysMenu::getMenuStatus, 1)
                .eq(SysMenu::getIsProtected, 0)
                .in(SysMenu::getTenantId, List.of("#", tenantId))
                .in(SysMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    /**
     * 列表查询
     */
    public List<SysMenu> list(String menuName, Integer menuStatus) {
        return lambdaQuery()
                .eq(menuStatus != null, SysMenu::getMenuStatus, menuStatus)
                .like(StringUtils.isNotBlank(menuName), SysMenu::getMenuName, menuName)
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    /**
     * Api菜单权限
     */
    public List<SysMenu> listApiPermitsByAdmin(String tenantId) {
        return lambdaQuery()
                .in(SysMenu::getTenantId, List.of("#", tenantId))
                .eq(SysMenu::getMenuStatus, 1)
                .and(wrapper -> wrapper.isNotNull(SysMenu::getMenuPermit).or().eq(SysMenu::getMenuType, "M"))
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    /**
     * 修改菜单信息
     */
    public void updateMenu(SysMenu sysMenu) {
        lambdaUpdate().eq(SysMenu::getMenuId, sysMenu.getMenuId())
                .set(SysMenu::getTenantId, sysMenu.getTenantId())
                .set(SysMenu::getParentId, sysMenu.getParentId())
                .set(SysMenu::getMenuName, sysMenu.getMenuName())
                .set(SysMenu::getMenuOrder, sysMenu.getMenuOrder())
                .set(SysMenu::getMenuPermit, sysMenu.getMenuPermit())
                .set(SysMenu::getMenuPath, sysMenu.getMenuPath())
                .set(SysMenu::getMenuParam, sysMenu.getMenuParam())
                .set(SysMenu::getMenuType, sysMenu.getMenuType())
                .set(SysMenu::getMenuIcon, sysMenu.getMenuIcon())
                .set(SysMenu::getMenuStatus, sysMenu.getMenuStatus())
                .set(SysMenu::getComponent, sysMenu.getComponent())
                .set(SysMenu::getIsFrame, sysMenu.getIsFrame())
                .set(SysMenu::getIsCache, sysMenu.getIsCache())
                .set(SysMenu::getIsVisible, sysMenu.getIsVisible())
                .set(SysMenu::getIsProtected, sysMenu.getIsProtected())
                .set(SysMenu::getRemark, sysMenu.getRemark())
                .set(SysMenu::getUpdateBy, Access.userCode())
                .set(SysMenu::getUpdateTime, new Date())
                .update();
    }

    /**
     * 获取菜单权限符
     */
    public List<String> queryPermitsByIds(List<Integer> menuIds) {
        List<SysMenu> menuList = lambdaQuery()
                .in(SysMenu::getMenuId, menuIds)
                .select(SysMenu::getMenuPermit)
                .list();
        return Collections.filterCopyToList(menuList, SysMenu::getMenuPermit, Objects::nonNull);
    }
}
