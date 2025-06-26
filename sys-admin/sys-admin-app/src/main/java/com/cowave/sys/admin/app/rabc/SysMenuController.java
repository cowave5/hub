/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.rabc;

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.service.rabc.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 菜单
 * @order 5
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/menu")
public class SysMenuController{

	private final SysMenuService sysMenuService;

	/**
	 * 菜单树
	 */
	@GetMapping("/tree")
	public Response<List<Tree<Integer>>> tree(){
		return Response.success(sysMenuService.tree(Access.tenantId()));
	}

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:menu:query')")
	@GetMapping
	public Response<Response.Page<SysMenu>> list(String menuName, Integer menuStatus){
		return Response.page(sysMenuService.list(menuName, menuStatus));
	}

	/**
     * 详情
     *
     * @param menuId 菜单id
     */
	@PreAuthorize("@permit.hasPermit('sys:menu:query')")
    @GetMapping("/{menuId}")
    public Response<SysMenu> info(@PathVariable Integer menuId) {
        return Response.success(sysMenuService.info(menuId));
    }

    /**
     * 新增
     */
	@PreAuthorize("@permit.hasPermit('sys:menu:create')")
    @PostMapping
    public Response<Long> add(@Validated @RequestBody SysMenu sysMenu) throws Exception {
        return Response.success(() -> sysMenuService.add(sysMenu));
    }

	/**
     * 删除
     *
     * @param menuId 菜单id
     */
	@PreAuthorize("@permit.hasPermit('sys:menu:delete')")
    @DeleteMapping("/{menuId}")
    public Response<Void> delete(@PathVariable Integer menuId) throws Exception {
        return Response.success(() -> sysMenuService.delete(menuId));
    }

    /**
     * 修改
     */
	@PreAuthorize("@permit.hasPermit('sys:menu:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody SysMenu sysMenu) throws Exception {
        return Response.success(() -> sysMenuService.edit(sysMenu));
    }

	/**
	 * 导出菜单
	 */
	@PreAuthorize("@permit.hasPermit('sys:menu:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("菜单数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		List<SysMenu> menuList = sysMenuService.list(null, null);
		EasyExcel.write(response.getOutputStream(), SysMenu.class)
		.sheet("菜单").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(menuList);
	}
}
