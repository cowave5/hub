package com.cowave.sys.job.app.controller;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.ClientRegistry;
import com.cowave.sys.job.domain.client.TriggerResponse;
import com.cowave.sys.job.service.JobClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户端
 *
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class JobClientController {

    private final JobClientService jobClientService;

    /**
     * 注册
     */
    @PostMapping("/registry")
    public Response<Void> registry(@Validated @RequestBody ClientRegistry clientRegistry) throws Exception {
        return Response.success(() -> jobClientService.registry(clientRegistry));
    }

    /**
     * 注销
     */
    @PostMapping("/unregistry")
    public Response<Void> unRegistry(@Validated @RequestBody ClientRegistry clientRegistry) throws Exception {
        return Response.success(() -> jobClientService.unRegistry(clientRegistry));
    }

    /**
     * 回调
     */
    @PostMapping("/callback")
    public Response<Void> callback(@Validated @RequestBody List<TriggerResponse> list) throws Exception {
        return Response.success(() -> jobClientService.callback(list));
    }
}
