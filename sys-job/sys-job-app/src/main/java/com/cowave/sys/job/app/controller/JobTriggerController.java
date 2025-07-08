package com.cowave.sys.job.app.controller;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.domain.request.JobTriggerQuery;
import com.cowave.sys.job.service.JobTriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务
 *
 * @author xuxueli/shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trigger")
public class JobTriggerController {

    private final JobTriggerService jobTriggerService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('sys:job:query')")
    @GetMapping
    public Response<Response.Page<JobTrigger>> list(JobTriggerQuery query) {
        return Response.page(jobTriggerService.pageList(query));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permits.hasPermit('sys:job:create')")
    @PostMapping
    public Response<Void> create(@RequestBody JobTrigger jobTrigger) throws Exception {
        return Response.success(() -> jobTriggerService.create(jobTrigger));
    }

    /**
     * 删除
     *
     * @param jobIds 任务id列表
     */
    @PreAuthorize("@permits.hasPermit('sys:job:delete')")
    @DeleteMapping("/{jobIds}")
    public Response<Void> delete(@PathVariable List<Integer> jobIds) throws Exception {
        return Response.success(() -> jobTriggerService.delete(jobIds));
    }

    /**
     * 立即执行
     *
     * @param id 任务id
     */
    @PreAuthorize("@permits.hasPermit('sys:job:status')")
    @PostMapping("/exec/{id}")
    public Response<Void> exec(@PathVariable("id") Integer id) throws Exception {
        return Response.success(() -> jobTriggerService.exec(id));
    }

    /**
     * 切换状态
     *
     * @param id 任务id
     * @param status 任务状态
     */
    @PreAuthorize("@permits.hasPermit('sys:job:status')")
    @PatchMapping("/status/{id}/{status}")
    public Response<Void> switchStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status) throws Exception {
        return Response.success(() -> jobTriggerService.switchStatus(id, status));
    }
}
