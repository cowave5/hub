/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.delegate;

import com.cowave.sys.admin.api.entity.flow.Leave;
import com.cowave.sys.admin.api.mapper.FlowLeaveMapper;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.stereotype.Component;

/**
 * 请假同意处理
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class LeaveApprovedDelegate implements JavaDelegate {

    private final HistoryService historyService;

    private final FlowLeaveMapper flowLeaveMapper;

    @Override
    public void execute(DelegateExecution execution) {
        HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance process = historyQuery.processInstanceId(execution.getProcessInstanceId()).singleResult();
        Long leaveId = Long.valueOf(process.getBusinessKey());
        flowLeaveMapper.changeProcessStatus(leaveId, Leave.PROCESS_STATUS_APPROVED);
    }
}
