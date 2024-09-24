/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.api.entity.flow.Leave;
import com.cowave.sys.admin.api.entity.flow.Purchase;
import com.cowave.sys.admin.api.mapper.FlowPurchaseMapper;
import com.cowave.sys.admin.api.mapper.SysUserMapper;
import com.cowave.sys.admin.api.service.FlowPurchaseService;
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

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Transactional
@Service
public class FlowPurchaseServiceImpl implements FlowPurchaseService {

    private final FlowPurchaseMapper flowPurchaseMapper;

    private final SysUserMapper sysUserMapper;

    private final RuntimeService runtimeService;

    private final IdentityService identityService;

    private final HistoryService historyService;

    private final TaskService taskService;

    @Override
    public Page<Purchase> list(Purchase purchase) {
        Page<Purchase> page = flowPurchaseMapper.list(Access.page(), purchase);
        List<Purchase> list = page.getRecords();
        for(Purchase p : list){
            Task activeTask = taskService.createTaskQuery().processInstanceId(p.getProcessId()).active().singleResult();
            if(activeTask != null){
                p.setTaskId(activeTask.getId());
                p.setProcessTaskUser(sysUserMapper.queryNameById(Long.valueOf(activeTask.getAssignee())));
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
        identityService.setAuthenticatedUserId(String.valueOf(Access.userId()));
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
