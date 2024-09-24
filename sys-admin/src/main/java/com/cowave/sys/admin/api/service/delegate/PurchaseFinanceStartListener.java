/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.delegate;

import com.cowave.sys.admin.api.entity.flow.Purchase;
import com.cowave.sys.admin.api.mapper.FlowPurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 财务审批开始
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class PurchaseFinanceStartListener implements TaskListener {

    private final HistoryService historyService;

    private final FlowPurchaseMapper flowPurchaseMapper;

    public void notify(DelegateTask delegateTask) {
        HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance process = historyQuery.processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
        Long purchaseId = Long.valueOf(process.getBusinessKey());
        flowPurchaseMapper.changeProcessStatus(purchaseId, Purchase.STATUS_FINANCE);
    }
}
