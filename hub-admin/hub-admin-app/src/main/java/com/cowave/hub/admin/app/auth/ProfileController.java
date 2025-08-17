/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.auth;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.auth.dto.UserProfile;
import com.cowave.hub.admin.domain.auth.request.TokenRequest;
import com.cowave.hub.admin.domain.auth.request.MfaBind;
import com.cowave.hub.admin.domain.auth.request.PasswdReset;
import com.cowave.hub.admin.domain.auth.request.ProfileUpdate;
import com.cowave.hub.admin.domain.auth.vo.TokenVo;
import com.cowave.hub.admin.domain.auth.vo.MfaVo;
import com.cowave.hub.admin.service.auth.HubTokenService;
import com.cowave.hub.admin.service.auth.ProfileService;
import com.cowave.hub.admin.service.rabc.HubMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 个人信息
 * @order 12
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final HubTokenService hubTokenService;
    private final HubMenuService hubMenuService;

    /**
     * 详情
     */
    @GetMapping
    public Response<UserProfile> info() throws Exception {
        return Response.success(profileService.info());
    }

    /**
     * 修改
     */
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody ProfileUpdate profile) throws Exception {
        return Response.success(() -> profileService.edit(profile));
    }

    /**
     * 重置密码
     */
    @PatchMapping(value = {"/passwd"})
    public Response<Void> resetPasswd(@RequestBody PasswdReset passwdReset) throws Exception {
        return Response.success(() -> profileService.resetPasswd(passwdReset));
    }

	/**
     * MFA获取
     */
    @GetMapping("/mfa")
    public Response<MfaVo> generateMfa() {
        return Response.success(profileService.generateMfa());
    }

    /**
     * MFA绑定
     */
    @PatchMapping("/mfa/enable")
    public Response<Void> enableMfa(@Validated @RequestBody MfaBind mfaBind) throws Exception {
        return Response.success(() -> profileService.enableMfa(mfaBind));
    }

    /**
     * MFA解除
     */
    @PatchMapping("/mfa/disable")
    public Response<Void> disableMfa(@Validated @RequestBody MfaBind mfaBind) throws Exception {
        return Response.success(() -> profileService.disableMfa(mfaBind));
    }

    /**
	 * Api令牌权限树
	 */
	@GetMapping("/api/tree")
	public Response<List<Tree<Integer>>> getApiTree(){
		return Response.success(hubMenuService.getApiPermitsByUser(Access.tenantId()));
	}

    /**
	 * Api令牌列表
	 */
	@GetMapping("/api/token")
	public Response<List<TokenVo>> listApiToken() {
		return Response.success(hubTokenService.listApiToken());
	}

    /**
	 * 创建Api令牌
	 */
	@PostMapping("/api/token")
	public Response<String> creatApiToken(@RequestBody TokenRequest request) {
		return Response.success(hubTokenService.creatApiToken(request));
	}

    /**
	 * 删除Api令牌
	 */
	@DeleteMapping("/api/token/{tokenId}")
	public Response<Void> deleteApiToken(@PathVariable Integer tokenId) throws Exception {
		return Response.success(() -> hubTokenService.deleteApiToken(tokenId));
	}
}
