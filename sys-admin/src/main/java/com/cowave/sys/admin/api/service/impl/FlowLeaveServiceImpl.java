/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.api.entity.flow.Leave;
import com.cowave.sys.admin.api.mapper.FlowLeaveMapper;
import com.cowave.sys.admin.api.mapper.SysUserMapper;
import com.cowave.sys.admin.api.service.FlowLeaveService;
import com.cowave.sys.admin.api.service.flow.FlowInstanceService;
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
public class FlowLeaveServiceImpl implements FlowLeaveService {

    private final FlowLeaveMapper flowLeaveMapper;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final IdentityService identityService;

    private final HistoryService historyService;

    private final SysUserMapper sysUserMapper;

    private final FlowInstanceService flowInstanceService;

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
                    l.setProcessTaskUser(sysUserMapper.queryNameById(Long.valueOf(activeTask.getAssignee())));
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
        identityService.setAuthenticatedUserId(String.valueOf(Access.userId()));
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
