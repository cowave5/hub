package com.cowave.sys.meter.app.env;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.meter.domain.env.EnvCredential;
import com.cowave.sys.meter.domain.env.request.CredentialQuery;
import com.cowave.sys.meter.service.env.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 凭据
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/credential")
public class CredentialController {

    private final CredentialService credentialService;

    /**
	 * 列表
	 */
	@GetMapping
	public Response<Response.Page<EnvCredential>> list(CredentialQuery query) {
		return Response.page(credentialService.list(query));
	}

    /**
	 * 详情
	 */
	@GetMapping("/{credentialId}")
	public Response<EnvCredential> info(@PathVariable Integer credentialId) {
		return Response.success(credentialService.info(credentialId));
	}

    /**
     * 新增
     */
    @PostMapping
    public Response<Void> create(@Validated @RequestBody EnvCredential credential) throws Exception {
        return Response.success(() -> credentialService.create(credential));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{credentialIds}")
    public Response<Void> delete(@PathVariable List<Integer> credentialIds) throws Exception {
        return Response.success(() -> credentialService.delete(credentialIds));
    }

    /**
     * 修改
     */
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody EnvCredential credential) throws Exception {
        return Response.success(() -> credentialService.edit(credential));
    }
}
