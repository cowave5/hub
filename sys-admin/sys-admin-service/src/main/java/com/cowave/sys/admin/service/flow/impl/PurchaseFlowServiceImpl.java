/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.flow.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.flow.Purchase;
import com.cowave.sys.admin.infra.flow.mapper.FlowPurchaseMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.service.flow.PurchaseFlowService;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class PurchaseFlowServiceImpl implements PurchaseFlowService {
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final IdentityService identityService;
    private final SysUserDao sysUserDao;
    private final FlowPurchaseMapper flowPurchaseMapper;

    @Override
    public Page<Purchase> list(Purchase purchase) {
        Page<Purchase> page = flowPurchaseMapper.list(Access.page(), purchase);
        List<Purchase> list = page.getRecords();
        for(Purchase p : list){
            Task activeTask = taskService.createTaskQuery().processInstanceId(p.getProcessId()).active().singleResult();
            if(activeTask != null){
                p.setTaskId(activeTask.getId());
                p.setProcessTaskUser(sysUserDao.queryNameById(Integer.valueOf(activeTask.getAssignee())));
            }
        }
        return page;
    }

    @Override
    public Purchase info(Long id) {
        Purchase purchase = flowPurchaseMapper.info(id);
        List<HistoricVariableInstance> variables =
                historyService.createHistoricVariableInstanceQuery().processInstanceId(purchase.getProcessId()).list();
        Map<String, Object> variableMap = new HashMap<>();
        variables.forEach(v -> variableMap.put(v.getVariableName(), v.getValue()));
        purchase.setProcessVariables(variableMap);
        return purchase;
    }

    @Override
    public void add(Purchase purchase) {
        Long purchaseId = flowPurchaseMapper.nextId();
        // 启动采购流程
        identityService.setAuthenticatedUserId(Access.userId(String::valueOf));
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("starter", Access.userId());
        variables.put("manager", purchase.getManager());
        variables.put("finance", purchase.getFinance());
        variables.put("cashier", purchase.getCashier());
        variables.put("general", purchase.getGeneral());
        variables.put("money", purchase.getMoney());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("purchase", String.valueOf(purchaseId), variables);
        purchase.setId(purchaseId);
        purchase.setApplyUser(Access.userId());
        purchase.setApplyTime(new Date());
        purchase.setProcessId(process.getProcessInstanceId());
        flowPurchaseMapper.insert(purchase);
    }

    @Override
    public void edit(Purchase purchase) {
        flowPurchaseMapper.update(purchase);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            Purchase purchase = info(id);
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(purchase.getProcessId()).singleResult();
            if (process != null) {
                runtimeService.deleteProcessInstance(process.getId(), "删除");
            }
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(purchase.getProcessId()).singleResult();

            if (history != null) {
                historyService.deleteHistoricProcessInstance(history.getId());
            }

        }
        flowPurchaseMapper.delete(ids);
    }
}
