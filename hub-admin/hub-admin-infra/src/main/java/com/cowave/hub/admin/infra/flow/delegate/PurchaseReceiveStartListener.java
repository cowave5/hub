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

import com.cowave.hub.admin.domain.flow.Purchase;
import com.cowave.hub.admin.infra.flow.mapper.FlowPurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 收货开始
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class PurchaseReceiveStartListener implements TaskListener {

    private final HistoryService historyService;

    private final FlowPurchaseMapper flowPurchaseMapper;

    public void notify(DelegateTask delegateTask) {
        HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance process = historyQuery.processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
        Long purchaseId = Long.valueOf(process.getBusinessKey());
        flowPurchaseMapper.changeProcessStatus(purchaseId, Purchase.STATUS_RECEIVING);
    }
}
