/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.rabc.enums.EnableStatus;
import com.cowave.hub.admin.domain.rabc.HubMenu;
import com.cowave.hub.admin.infra.rabc.mapper.HubMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shanhuiming
 */
@Repository
public class HubMenuDao extends ServiceImpl<HubMenuMapper, HubMenu> {

    /**
     * 菜单权限（管理员）
     */
    public List<HubMenu> listMenusByAdmin(String tenantId) {
        return lambdaQuery()
                .eq(HubMenu::getIsVisible, 1)
                .eq(HubMenu::getMenuStatus, 1)
                .in(HubMenu::getTenantId, List.of("#", tenantId))
                .in(HubMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(HubMenu::getParentId, HubMenu::getMenuOrder)
                .list();
    }

    /**
     * 菜单权限（公共菜单）
     */
    public List<HubMenu> listMenusInPublic(String tenantId) {
        return lambdaQuery()
                .eq(HubMenu::getIsVisible, 1)
                .eq(HubMenu::getMenuStatus, 1)
                .eq(HubMenu::getIsProtected, 0)
                .in(HubMenu::getTenantId, List.of("#", tenantId))
                .in(HubMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(HubMenu::getParentId, HubMenu::getMenuOrder)
                .list();
    }

    /**
     * 列表查询
     */
    public List<HubMenu> list(String menuName, EnableStatus menuStatus) {
        return lambdaQuery()
                .eq(menuStatus != null, HubMenu::getMenuStatus, menuStatus)
                .like(StringUtils.isNotBlank(menuName), HubMenu::getMenuName, menuName)
                .orderByAsc(HubMenu::getParentId, HubMenu::getMenuOrder)
                .list();
    }

    /**
     * 修改菜单信息
     */
    public void updateMenu(HubMenu hubMenu) {
        lambdaUpdate().eq(HubMenu::getMenuId, hubMenu.getMenuId())
                .set(HubMenu::getTenantId, hubMenu.getTenantId())
                .set(HubMenu::getParentId, hubMenu.getParentId())
                .set(HubMenu::getMenuName, hubMenu.getMenuName())
                .set(HubMenu::getMenuOrder, hubMenu.getMenuOrder())
                .set(HubMenu::getMenuPermit, hubMenu.getMenuPermit())
                .set(HubMenu::getMenuPath, hubMenu.getMenuPath())
                .set(HubMenu::getMenuParam, hubMenu.getMenuParam())
                .set(HubMenu::getMenuType, hubMenu.getMenuType())
                .set(HubMenu::getMenuIcon, hubMenu.getMenuIcon())
                .set(HubMenu::getMenuStatus, hubMenu.getMenuStatus())
                .set(HubMenu::getComponent, hubMenu.getComponent())
                .set(HubMenu::getIsFrame, hubMenu.getIsFrame())
                .set(HubMenu::getIsCache, hubMenu.getIsCache())
                .set(HubMenu::getIsVisible, hubMenu.getIsVisible())
                .set(HubMenu::getIsProtected, hubMenu.getIsProtected())
                .set(HubMenu::getRemark, hubMenu.getRemark())
                .set(HubMenu::getUpdateBy, Access.userCode())
                .set(HubMenu::getUpdateTime, new Date())
                .update();
    }

    /**
     * 获取菜单权限符
     */
    public List<String> queryPermitsByIds(List<Integer> menuIds) {
        List<HubMenu> menuList = lambdaQuery()
                .in(HubMenu::getMenuId, menuIds)
                .select(HubMenu::getMenuPermit)
                .list();
        return Collections.filterCopyToList(menuList, HubMenu::getMenuPermit, Objects::nonNull);
    }
}
