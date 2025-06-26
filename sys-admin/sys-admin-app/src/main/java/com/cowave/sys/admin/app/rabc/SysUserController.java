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
import com.cowave.commons.client.http.asserts.I18Messages;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.write.DropdownWriteHandler;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserListDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.service.rabc.SysUserService;
import com.cowave.sys.admin.domain.rabc.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 *
 * @author shanhuiming
 * @order 3
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:user:query')")
    @GetMapping
    public Response<Response.Page<UserListDto>> list(UserQuery query) {
        return Response.success(sysUserService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('sys:user:query')")
    @GetMapping("/{userId}")
    public Response<UserInfoDto> info(@PathVariable Integer userId) {
        return Response.success(sysUserService.info(Access.tenantId(), userId));
    }

    /**
     * 新增
     */
    @Operation(module = "op_admin", type = "op_user", action = "op_create", desc = "新增用户：#{#user.userName}")
    @PreAuthorize("@permit.hasPermit('sys:user:create')")
    @PostMapping
    public Response<Void> create(@Validated @RequestBody UserCreate user) throws Exception {
        return Response.success(() -> sysUserService.create(Access.tenantId(), user));
    }

    /**
     * 删除
     *
     * @param userIds id列表
     */
    @Operation(module = "op_admin", type = "op_user", action = "op_delete", desc = "删除用户")
    @PreAuthorize("@permit.hasPermit('sys:user:delete')")
    @DeleteMapping("/{userIds}")
    public Response<Void> delete(@PathVariable List<Integer> userIds) throws Exception {
        return Response.success(() -> sysUserService.delete(Access.tenantId(), userIds));
    }

    /**
     * 修改
     */
    @Operation(module = "op_admin", type = "op_user", action = "op_edit", desc = "修改用户：#{#user.userName}")
    @PreAuthorize("@permit.hasPermit('sys:user:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody UserCreate user) throws Exception {
        return Response.success(() -> sysUserService.edit(Access.tenantId(), user));
    }

    /**
     * 修改角色
     */
    @Operation(module = "op_admin", type = "op_user", action = "op_grant", desc = "修改用户角色：#{#user.userName}")
    @PreAuthorize("@permit.hasPermit('sys:user:grant')")
    @PatchMapping("/roles")
    public Response<Void> changeRoles(@Validated @RequestBody UserRoleUpdate user) throws Exception {
        return Response.success(() -> sysUserService.changeRoles(Access.tenantId(), user));
    }

    /**
     * 修改状态
     */
    @Operation(module = "op_admin", type = "op_user", action = "op_status", desc = "修改用户状态：#{#user.userName}")
    @PreAuthorize("@permit.hasPermit('sys:user:status')")
    @PatchMapping("/status")
    public Response<Void> changeStatus(@Validated @RequestBody UserStatusUpdate user) throws Exception {
        return Response.success(() -> sysUserService.changeStatus(Access.tenantId(), user));
    }

    /**
     * 修改密码
     */
    @Operation(module = "op_admin", type = "op_user", action = "op_passwd", desc = "修改用户密码：#{#user.userName}")
    @PreAuthorize("@permit.hasPermit('sys:user:passwd')")
    @PatchMapping("/passwd")
    public Response<Void> changePasswd(@Validated @RequestBody UserPasswdUpdate user) throws Exception {
        return Response.success(() -> sysUserService.changePasswd(Access.tenantId(), user));
    }

    /**
     * 导入用户
     */
    @PreAuthorize("@permit.hasPermit('sys:user:import')")
    @PostMapping("/import")
    public Response<Void> importUser(MultipartFile file, boolean updateSupport) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            List<SysUser> list = EasyExcel.read(inputStream).head(SysUser.class).sheet().doReadSync();
            sysUserService.importUsers(Access.tenantId(), list, updateSupport);
        }
        return Response.success(null, I18Messages.msg("admin.import.success"));
    }

    /**
     * 导出用户
     */
    @PreAuthorize("@permit.hasPermit('sys:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserExportQuery query) throws Exception {
        String fileName = URLEncoder.encode("用户数据", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<SysUser> userList = sysUserService.listForExport(Access.tenantId(), query);
        EasyExcel.write(response.getOutputStream(), SysUser.class).sheet("用户")
                .registerWriteHandler(new ExcelIgnoreStyle()).doWrite(userList);
    }

    /**
     * 导出模板
     */
    @PreAuthorize("@permit.hasPermit('sys:user:export')")
    @PostMapping("/export/template")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        String fileName = URLEncoder.encode("test", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        DropdownWriteHandler sexHandler =
                new DropdownWriteHandler(List.of("男", "女", "未知"), 3, 1000);
        DropdownWriteHandler statusHandler =
                new DropdownWriteHandler(List.of("启用", "停用"), 6, 1000);
        EasyExcel.write(response.getOutputStream(), SysUser.class)
                .sheet("用户")
                .registerWriteHandler(sexHandler)
                .registerWriteHandler(statusHandler)
                .doWrite(new ArrayList<>());
    }

    /**
     * 用户组织架构
     */
    @PreAuthorize("@permit.hasPermit('sys:user:diagram')")
    @GetMapping("/diagram")
    public Response<Tree<Integer>> getDiagram() {
        return Response.success(sysUserService.getDiagram(Access.tenantId()));
    }

    /**
     * 用户流程候选人
     *
     * @param userId 用户id
     */
    @GetMapping("/candidates")
    public Response<List<UserNameDto>> getUserCandidates(Integer userId) {
        return Response.success(sysUserService.getUserCandidates(Access.tenantId(), userId));
    }

    /**
     * 用户名称查询
     */
    @GetMapping("/name/{userIds}")
    public Response<List<String>> getNamesById(@PathVariable List<Integer> userIds) {
        return Response.success(sysUserService.getNamesById(Access.tenantId(), userIds));
    }
}
