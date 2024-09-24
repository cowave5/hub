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
import com.cowave.sys.admin.api.entity.flow.*;
import com.cowave.sys.admin.api.mapper.FlowExecutionMapper;
import com.cowave.sys.admin.api.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.*;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class FlowInstanceService {

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final HistoryService historyService;

    private final RepositoryService repositoryService;

    private final ProcessEngineConfiguration engineConfiguration;

    private final FlowExecutionMapper executionMapper;

    private final SysUserMapper sysUserMapper;

    /**
     * 列表
     */
    public Response.Page<FlowInstance> list(FlowInstance flowInstance) {
        List<FlowInstance> list = new ArrayList<>();
        if(flowInstance.isActive()){
            ProcessInstanceQuery instanceQuery = runtimeService.createProcessInstanceQuery();
            flowInstance.fillInstanceQuery(instanceQuery);
            List<ProcessInstance> processList =
                    instanceQuery.orderByProcessDefinitionId().desc().listPage(Access.pageOffset(), Access.pageSize());
            processList.forEach(process -> {
                FlowInstance instance = new FlowInstance();
                instance.setInstanceId(process.getProcessInstanceId());
                instance.setInstanceName(process.getProcessDefinitionName());
                instance.setBusinessKey(process.getBusinessKey());
                instance.setBeginTime(process.getStartTime());
                instance.setStartUser(process.getStartUserId());
                instance.setStartUserName(sysUserMapper.queryNameById(Long.valueOf(process.getStartUserId())));
                instance.setSuspended(process.isSuspended());
                instance.setEnded(process.isEnded());
                // 当前任务
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(process.getProcessInstanceId()).active().list();
                List<FlowTask> flowTaskList = new ArrayList<>();
                for(Task task : taskList){
                    FlowTask flowTask = new FlowTask();
                    flowTask.setTaskName(task.getName());
                    flowTask.setAssignee(task.getAssignee());
                    flowTask.setAssigneeName(sysUserMapper.queryNameById(Long.valueOf(task.getAssignee())));
                    flowTaskList.add(flowTask);
                }
                instance.setTaskList(flowTaskList);
                list.add(instance);
            });
            return new Response.Page<>(list, instanceQuery.count());
        }else{
            HistoricProcessInstanceQuery historyQuery = historyService.createHistoricProcessInstanceQuery();
            flowInstance.fillHistoryQuery(historyQuery);
            List<HistoricProcessInstance> historyList =
                    historyQuery.orderByProcessInstanceStartTime().desc().listPage(Access.pageOffset(), Access.pageSize());
            historyList.forEach(history -> {
                FlowInstance instance = new FlowInstance();
                instance.setInstanceId(history.getId());
                instance.setInstanceName(history.getProcessDefinitionName());
                instance.setBusinessKey(history.getBusinessKey());
                instance.setBeginTime(history.getStartTime());
                instance.setStartUser(history.getStartUserId());
                instance.setStartUserName(sysUserMapper.queryNameById(Long.valueOf(history.getStartUserId())));
                if (history.getEndTime() == null) {
                    List<Task> taskList =
                            taskService.createTaskQuery().processInstanceId(history.getId()).active().list();
                    List<FlowTask> flowTaskList = new ArrayList<>();
                    for(Task task : taskList){
                        FlowTask flowTask = new FlowTask();
                        flowTask.setTaskName(task.getName());
                        flowTask.setAssignee(task.getAssignee());
                        flowTask.setAssigneeName(sysUserMapper.queryNameById(Long.valueOf(task.getAssignee())));
                        flowTaskList.add(flowTask);
                    }
                    instance.setTaskList(flowTaskList);
                }else{
                    instance.setEnded(true);
                    instance.setEndTime(history.getEndTime());
                }
                list.add(instance);
            });
            return new Response.Page<>(list, historyQuery.count());
        }
    }

    /**
     * 流程进度
     */
    public String progress(String id) throws IOException {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(id).singleResult();
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(id).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedActivities = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            String activityId = activityInstance.getActivityId();
            highLightedActivities.add(activityId);
            if ("sequenceFlow".equals(activityInstance.getActivityType())) {
                highLightedFlows.add(activityInstance.getActivityId());
            }
        }
        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessEngineConfiguration engine = engineConfiguration.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engine.getProcessDiagramGenerator();
        try(InputStream input = diagramGenerator.generateDiagram(bpmnModel, "bmp", highLightedActivities, highLightedFlows,
                "宋体", "宋体", "宋体", engine.getClassLoader(), 1.0, true);
            ByteArrayOutputStream output = new ByteArrayOutputStream()){
            IOUtils.copy(input, output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
    }

    /**
     * 流程记录
     */
    public Response.Page<FlowTask> history(String id) {
        List<HistoricActivityInstance> historyList = historyService.createHistoricActivityInstanceQuery().activityType("userTask")
                .processInstanceId(id).orderByHistoricActivityInstanceStartTime().asc().listPage(Access.pageOffset(), Access.pageSize());
        List<FlowTask> list  = new ArrayList<>();
        historyList.forEach( history -> {
            FlowTask task = new FlowTask();
            task.setTaskId(history.getTaskId());
            task.setTaskName(history.getActivityName());
            task.setStartTime(history.getStartTime());
            task.setEndTime(history.getEndTime());
            task.setProcessInstanceId(history.getProcessInstanceId());
            task.setAssignee(history.getAssignee());
            task.setAssigneeName(sysUserMapper.queryNameById(Long.valueOf(history.getAssignee())));
            List<Comment> comments = taskService.getTaskComments(history.getTaskId());
            if (comments.size() > 0) {
                task.setComment(comments.get(0).getFullMessage());
            }
            list.add(task);
        });
        long total = historyService.createHistoricActivityInstanceQuery().processInstanceId(id).activityType("userTask").count();
        return new Response.Page<>(list, total);
    }

    /**
     * 执行实例
     */
    public List<FlowInstance> executions(String name) {
        List<FlowExecution> executionList = executionMapper.selectActRuExecutionListByProcessName(name);
        List<FlowInstance> flows = new ArrayList<>();
        executionList.forEach(execution -> {
            FlowInstance info = new FlowInstance();
            info.setExecutionId(execution.getId());
            info.setInstanceId(execution.getProcInstId());
            info.setBeginTime(execution.getStartTime());
            info.setSuspended(execution.getSuspensionState() != 1L);
            info.setActive(execution.getIsActive() != 0);
            ProcessInstance process = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcInstId()).singleResult();
            if (execution.getActId() != null) {
                BpmnModel bpmnModel = repositoryService.getBpmnModel(process.getProcessDefinitionId());
                Map<String, FlowElement> nodes = bpmnModel.getMainProcess().getFlowElementMap();
                info.setTaskName(nodes.get(execution.getActId()).getName());
            } else {
                info.setBeginTime(process.getStartTime());
                info.setStartUser(process.getStartUserId());
            }
            info.setInstanceName(process.getProcessDefinitionName());
            if (execution.getParentId() == null) {
                info.setParentExecutionId("0");
            } else {
                info.setParentExecutionId(execution.getParentId());
            }
            flows.add(info);
        });
        return flows;
    }

    /**
     * 流程变量
     */
    public Response.Page<FlowVariable> variables(String id) {
        List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(id).orderByVariableName().asc().listPage(Access.pageOffset(), Access.pageSize());
        long total = historyService.createHistoricVariableInstanceQuery().processInstanceId(id).count();
        List<FlowVariable> list = new ArrayList<>();
        variables.forEach(v -> {
            FlowVariable variable = new FlowVariable();
            variable.setExecutionId(v.getExecutionId());
            variable.setVariableId(v.getId());
            variable.setValue(v.getValue());
            variable.setVariableName(v.getVariableName());
            variable.setVariableTypeName(v.getVariableTypeName());
            variable.setCreateTime(v.getCreateTime());
            variable.setLastUpdatedTime(v.getLastUpdatedTime());
            list.add(variable);
        });
        return new Response.Page<>(list, total);
    }

    /**
     * 流程挂起
     */
    public void suspend(String id) {
        runtimeService.suspendProcessInstanceById(id);
    }

    /**
     * 流程唤醒
     */
    public void activate(String id) {
        runtimeService.activateProcessInstanceById(id);
    }

    /**
     * 跳转流程
     */
    public void jump(String taskId, String sid, String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult().getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        // 当前节点
        FlowNode currentNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(activityId);
        List<SequenceFlow> currentFlows = new ArrayList<>(currentNode.getOutgoingFlows());
        // 目标节点及连线
        FlowNode targetNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(sid);
        SequenceFlow jumpFlow = new SequenceFlow();
        jumpFlow.setId("newFlow");
        jumpFlow.setSourceFlowElement(currentNode);
        jumpFlow.setTargetFlowElement(targetNode);
        List<SequenceFlow> jumpFlowList = List.of(jumpFlow);
        // 设置新方向
        currentNode.setOutgoingFlows(jumpFlowList);
        // 完成当前任务
        taskService.addComment(taskId, task.getProcessInstanceId(), "comment", comment);
        taskService.complete(taskId);
        // 恢复原有方向
        currentNode.setOutgoingFlows(currentFlows);
    }

    /**
     * 修改流程变量
     */
    public void variablesEdit(FlowVariable flowVariable) {
        runtimeService.setVariable(flowVariable.getExecutionId(), flowVariable.getVariableName(), flowVariable.getValue());
    }

    /**
     * 删除流程变量
     */
    public void variablesDelete(FlowVariable flowVariable) {
        runtimeService.removeVariable(flowVariable.getExecutionId(), flowVariable.getVariableName());
    }

    /**
     * 流程删除
     */
    public void delete(String[] ids) {
        for (String processId : ids) {
            runtimeService.deleteProcessInstance(processId, "删除");
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
            if(history != null){
                historyService.deleteHistoricProcessInstance(processId);
            }
        }
    }

    /**
     * 撤销
     */
    public void revocate(String processId, String endNodeId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String definitionId = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId).singleResult().getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        // 当前任务节点和连线
        FlowNode currentNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(execution.getActivityId());
        List<SequenceFlow> currentFlows = new ArrayList<>(currentNode.getOutgoingFlows());
        // 结束节点连接
        FlowNode endNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(endNodeId);
        SequenceFlow revocateFlow = new SequenceFlow();
        revocateFlow.setId("endFlow");
        revocateFlow.setSourceFlowElement(currentNode);
        revocateFlow.setTargetFlowElement(endNode);
        List<SequenceFlow> revocateFlows = List.of(revocateFlow);
        // 设置新方向
        currentNode.setOutgoingFlows(revocateFlows);
        // 完成当前任务
        taskService.setAssignee(task.getId(), String.valueOf(Access.userId()));
        taskService.addComment(task.getId(), processId, "comment", "已撤销");
        taskService.complete(task.getId());
        // 恢复原有方向
        currentFlows.add(revocateFlow);
        currentNode.setOutgoingFlows(currentFlows);
    }
}
