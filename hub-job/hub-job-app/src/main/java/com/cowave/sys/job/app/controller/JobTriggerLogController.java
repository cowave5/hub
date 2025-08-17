package com.cowave.sys.job.app.controller;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.JobTriggerLog;
import com.cowave.sys.job.domain.request.JobTriggerLogQuery;
import com.cowave.sys.job.service.JobTriggerLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务日志
 *
 * @author xuxueli/shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trigger/log")
public class JobTriggerLogController {

    private final JobTriggerLogService jobTriggerLogService;

    /**
     * 列表
     */
    @GetMapping
    public Response<Response.Page<JobTriggerLog>> list(JobTriggerLogQuery query) {
        return Response.page(jobTriggerLogService.pageList(query));
    }
}
