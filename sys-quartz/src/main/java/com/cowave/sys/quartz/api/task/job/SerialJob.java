/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
