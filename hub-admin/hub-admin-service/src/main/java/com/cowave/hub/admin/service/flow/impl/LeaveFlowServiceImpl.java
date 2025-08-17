/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.flow.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.flow.Leave;
import com.cowave.hub.admin.infra.flow.mapper.FlowLeaveMapper;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.service.flow.FlowInstanceService;
import com.cowave.hub.admin.service.flow.LeaveFlowService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class LeaveFlowServiceImpl implements LeaveFlowService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final IdentityService identityService;
    private final HistoryService historyService;
    private final FlowInstanceService flowInstanceService;
    private final HubUserDao hubUserDao;
    private final FlowLeaveMapper flowLeaveMapper;

    @Override
    public Page<Leave> list(Leave leave) {
        Page<Leave> page = flowLeaveMapper.list(Access.page(), leave);
        List<Leave> list = page.getRecords();
        for(Leave l : list){
            if(Leave.PROCESS_STATUS_APPROVING.equals(l.getProcessStatus())){
                Task activeTask = taskService.createTaskQuery().processInstanceId(l.getProcessId()).active().singleResult();
                if(activeTask != null){
                    l.setTaskId(activeTask.getId());
                    l.setProcessTask(activeTask.getName());
                    l.setProcessTaskUser(hubUserDao.queryNameById(Integer.valueOf(activeTask.getAssignee())));
                }
            }
        }
        return page;
    }

    @Override
    public Leave info(Long id) {
        Leave leave = flowLeaveMapper.info(id);
        List<HistoricVariableInstance> variables =
                historyService.createHistoricVariableInstanceQuery().processInstanceId(leave.getProcessId()).list();
        Map<String, Object> variableMap = new HashMap<>();
        variables.forEach(v -> variableMap.put(v.getVariableName(), v.getValue()));
        leave.setProcessVariables(variableMap);
        return leave;
    }

    @Override
    public void add(Leave leave) {
        Long leaveId = flowLeaveMapper.nextId();
        // 启动流程
        identityService.setAuthenticatedUserId(Access.userId(String::valueOf));
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyUser", Access.userId());
        variables.put("deptApprover", leave.getDeptApprover());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("leave", String.valueOf(leaveId), variables);
        String processId = process.getProcessInstanceId();
        // 完成第一个任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskService.addComment(task.getId(), processId, "comment", "申请");
        taskService.complete(task.getId());
        // 保存请假信息
        leave.setId(leaveId);
        leave.setProcessId(processId);
        leave.setApplyUser(Access.userId());
        leave.setApplyTime(process.getStartTime());
        flowLeaveMapper.insert(leave);
    }

    @Override
    public int edit(Leave leave) {
        return flowLeaveMapper.update(leave);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            Leave leave = flowLeaveMapper.info(id);
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(leave.getProcessId()).singleResult();
            if(process != null){
                runtimeService.deleteProcessInstance(leave.getProcessId(), "删除");
            }
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(leave.getProcessId()).singleResult();
            if(history != null){
                historyService.deleteHistoricProcessInstance(leave.getProcessId());
            }
        }
        flowLeaveMapper.delete(ids);
    }

    @Override
    public void revocate(Long id) {
        Leave leave = flowLeaveMapper.info(id);
        flowInstanceService.revocate(leave.getProcessId(), "leaveRevocatedEnd");
    }
}
