/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.flow;

import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.api.entity.flow.FlowTask;
import com.cowave.sys.admin.api.entity.flow.TaskComplete;
import com.cowave.sys.admin.api.mapper.SysUserMapper;
import com.cowave.sys.admin.api.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class FlowTaskService {

    private final TaskService taskService;

    private final FormService formService;

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final SysNoticeService sysNoticeService;

    private final SysUserMapper sysUserMapper;

    /**
     * 全部待办
     */
    public Response.Page<FlowTask> listAll(FlowTask flowTask) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        flowTask.fillTaskQuery(taskQuery);
        return list(taskQuery);
    }

    /**
     * 我的待办
     */
    public Response.Page<FlowTask> list(FlowTask flowTask) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(Access.userId()));
        flowTask.fillTaskQuery(taskQuery);
        return list(taskQuery);
    }

    /**
     * 我办理的
     */
    public Response.Page<FlowTask> listHistory(FlowTask flowTask) {
        HistoricTaskInstanceQuery historyTaskQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(String.valueOf(Access.userId())).finished();
        flowTask.fillHistoryTaskQuery(historyTaskQuery);

        List<HistoricTaskInstance> tasks = historyTaskQuery
                .orderByTaskCreateTime().desc().listPage(Access.pageOffset(), Access.pageSize());
        List<FlowTask> list = new ArrayList<>();
        tasks.forEach(t -> {
            FlowTask task = new FlowTask();
            task.setTaskId(t.getId());
            task.setTaskName(t.getName());
            task.setExecutionId(t.getExecutionId());
            task.setAssignee(t.getAssignee());
            task.setAssigneeName(sysUserMapper.queryNameById(Long.valueOf(t.getAssignee())));
            task.setBeginTime(t.getCreateTime());
            task.setEndTime(t.getEndTime());
            task.setFormKey(t.getFormKey());
            List<Comment> comments = taskService.getTaskComments(t.getId());
            if (!comments.isEmpty()) {
                task.setComment(comments.get(0).getFullMessage());
            }
            task.setProcessInstanceId(t.getProcessInstanceId());
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
            if(process != null){
                task.setBusinessKey(process.getBusinessKey());
                task.setProcessName(process.getProcessDefinitionName());
                task.setStarter(process.getStartUserId());
                task.setStarterName(sysUserMapper.queryNameById(Long.valueOf(process.getStartUserId())));
                task.setStartTime(process.getStartTime());
            }else{
                HistoricProcessInstance historyProcess =
                        historyService.createHistoricProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
                task.setBusinessKey(historyProcess.getBusinessKey());
                task.setProcessName(historyProcess.getProcessDefinitionName());
                task.setStarter(historyProcess.getStartUserId());
                task.setStarterName(sysUserMapper.queryNameById(Long.valueOf(historyProcess.getStartUserId())));
                task.setStartTime(historyProcess.getStartTime());
            }
            list.add(task);
        });
        return new Response.Page<>(list, historyTaskQuery.count());
    }

    /**
     * 任务表单
     */
    public String form(String taskId) {
        return formService.getTaskFormData(taskId).getFormKey();
    }

    /**
     * 任务办理
     */
    public void complete(TaskComplete taskComplete) {
        String taskId = taskComplete.getTaskId();
        taskService.setAssignee(taskId, String.valueOf(Access.userId())); // 实际办理人
        if(StringUtils.isNotBlank(taskComplete.getComment())){
            String processId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
            taskService.addComment(taskId, processId, taskComplete.getComment());
        }
        if (MapUtils.isNotEmpty(taskComplete.getVariables())) {
            taskService.complete(taskId, taskComplete.getVariables());
        } else {
            taskService.complete(taskComplete.getTaskId());
        }
    }

    /**
     * 修改办理人
     */
    public void assignee(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    /**
     * 办理过程
     */
    public List<FlowTask> records(String taskId) {
        String processId;
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task != null){
            processId = task.getProcessInstanceId();
        }else{
            HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            processId = historicTask.getProcessInstanceId();
        }

        List<HistoricActivityInstance> activityHistory = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<FlowTask> list  = new ArrayList<>();
        activityHistory.forEach(activity -> {
            if("userTask".equals(activity.getActivityType())){
                FlowTask flowTask = new FlowTask();
                flowTask.setTaskType("userTask");
                flowTask.setProcessInstanceId(processId);
                flowTask.setTaskName(activity.getActivityName());
                flowTask.setAssignee(activity.getAssignee());
                flowTask.setAssigneeName(sysUserMapper.queryNameById(Long.valueOf(activity.getAssignee())));
                flowTask.setStartTime(activity.getStartTime());
                flowTask.setEndTime(activity.getEndTime());
                // 多次任务执行问题
                List<Comment> comments = taskService.getTaskComments(activity.getTaskId());
                if (!comments.isEmpty()) {
                    flowTask.setComment(comments.get(0).getFullMessage());
                }
                list.add(flowTask);
            } else if("endEvent".equals(activity.getActivityType())
                    && processId.equals(activity.getCalledProcessInstanceId())){
                FlowTask flowTask = new FlowTask();
                flowTask.setTaskType("endEvent");
                flowTask.setTaskName("结束");
                flowTask.setEndTime(activity.getEndTime());
                list.add(flowTask);
            }
        });
        return list;
    }

    /**
     * 催办
     */
    public void press(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance process = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        sysNoticeService.sendFlowNotice(process.getProcessDefinitionName(), task.getName(),
                Long.valueOf(process.getStartUserId()), Long.valueOf(task.getAssignee()));
    }

    private Response.Page<FlowTask> list(TaskQuery taskQuery){
        List<Task> tasks = taskQuery.active().orderByTaskCreateTime().desc().listPage(Access.pageOffset(), Access.pageSize());
        List<FlowTask> list = new ArrayList<>();
        tasks.forEach(t -> {
            FlowTask task = new FlowTask();
            task.setProcessInstanceId(t.getProcessInstanceId());
            task.setTaskId(t.getId());
            task.setTaskName(t.getName());
            task.setExecutionId(t.getExecutionId());
            task.setAssignee(t.getAssignee());
            task.setAssigneeName(sysUserMapper.queryNameById(Long.valueOf(t.getAssignee())));
            task.setBeginTime(t.getCreateTime());
            task.setDueTime(t.getDueDate());
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
            task.setBusinessKey(process.getBusinessKey());
            task.setProcessName(process.getProcessDefinitionName());
            task.setStarter(process.getStartUserId());
            task.setStarterName(sysUserMapper.queryNameById(Long.valueOf(process.getStartUserId())));
            task.setStartTime(process.getStartTime());
            task.setFormKey(formService.getTaskFormData(t.getId()).getFormKey());
            list.add(task);
        });
        return new Response.Page<>(list, taskQuery.active().count());
    }
}
