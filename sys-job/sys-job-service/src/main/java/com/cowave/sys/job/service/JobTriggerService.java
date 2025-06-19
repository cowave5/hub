package com.cowave.sys.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.domain.request.JobTriggerQuery;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
public interface JobTriggerService {

    /**
     * 分页查询
     */
    Page<JobTrigger> pageList(JobTriggerQuery query);

    /**
     * 新增
     */
    void create(JobTrigger jobTrigger);

    /**
     * 删除
     */
    void delete(List<Integer> jobIds);

    /**
     * 立即执行
     */
    void exec(Integer id);

    /**
     * 切换状态
     */
    void switchStatus(Integer id, Integer status);
}
