/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.flow.delegate;

import com.cowave.sys.admin.domain.flow.Purchase;
import com.cowave.sys.admin.infra.flow.mapper.FlowPurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.stereotype.Component;

/**
 * 重新申请
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class PurchaseReapplyListener implements ExecutionListener {

    private final HistoryService historyService;

    private final FlowPurchaseMapper flowPurchaseMapper;

    @Override
    public void notify(DelegateExecution execution) {
        HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance process = historyQuery.processInstanceId(execution.getProcessInstanceId()).singleResult();
        Long purchaseId = Long.valueOf(process.getBusinessKey());
        flowPurchaseMapper.changeProcessStatus(purchaseId, Purchase.STATUS_DEPT);
    }
}
