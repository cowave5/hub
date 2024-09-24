/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.job;

import com.cowave.sys.quartz.api.task.entity.QuartzTask;
import org.quartz.JobExecutionContext;

/**
 * 串行作业
 *
 * @author shanhuiming
 */
@org.quartz.DisallowConcurrentExecution
public class SerialJob extends AbstractJob {
    @Override
    protected void doExecute(JobExecutionContext context, QuartzTask quartzTask) throws Exception {
        invokeMethod(quartzTask);
    }
}
