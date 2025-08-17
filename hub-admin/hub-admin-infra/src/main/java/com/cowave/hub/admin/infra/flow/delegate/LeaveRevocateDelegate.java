/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.flow.delegate;

import com.cowave.hub.admin.domain.flow.Leave;
import com.cowave.hub.admin.infra.flow.mapper.FlowLeaveMapper;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.stereotype.Component;

/**
 * 请假撤销处理
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class LeaveRevocateDelegate implements JavaDelegate {

    private final HistoryService historyService;

    private final FlowLeaveMapper flowLeaveMapper;

    @Override
    public void execute(DelegateExecution execution) {
        HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance process = historyQuery.processInstanceId(execution.getProcessInstanceId()).singleResult();
        Long leaveId = Long.valueOf(process.getBusinessKey());
        flowLeaveMapper.changeProcessStatus(leaveId, Leave.PROCESS_STATUS_REVOCATE);
    }
}
