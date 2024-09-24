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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.api.entity.flow.Meeting;
import com.cowave.sys.admin.api.mapper.FlowMeetingMapper;
import com.cowave.sys.admin.api.service.FlowMeetingService;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
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
public class FlowMeetingServiceImpl implements FlowMeetingService {

    private final FlowMeetingMapper flowMeetingMapper;

    private final RuntimeService runtimeService;

    private final IdentityService identityService;

    private final HistoryService historyService;

    @Override
    public Page<Meeting> list(Meeting meeting) {
        return flowMeetingMapper.list(Access.page(), meeting);
    }

    @Override
    public Meeting info(Long id) {
        return flowMeetingMapper.info(id);
    }

    @Override
    public void add(Meeting meeting) {
        Long meetingId = flowMeetingMapper.nextId();
        // 启动流程
        identityService.setAuthenticatedUserId(String.valueOf(Access.userId()));
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("applyUser", Access.userId());
        variables.put("members", meeting.getMembers());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("meeting", String.valueOf(meetingId), variables);
        // 保存会议
        meeting.setId(meetingId);
        meeting.setApplyUser(Access.userId());
        meeting.setApplyTime(new Date());
        meeting.setProcessId(process.getProcessInstanceId());
        flowMeetingMapper.insert(meeting);
    }

    @Override
    public void edit(Meeting meeting) {
        flowMeetingMapper.update(meeting);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            Meeting meeting = flowMeetingMapper.info(id);
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(meeting.getProcessId()).singleResult();
            if(process != null){
                runtimeService.deleteProcessInstance(meeting.getProcessId(), "删除");
            }
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(meeting.getProcessId()).singleResult();
            if(history != null){
                historyService.deleteHistoricProcessInstance(meeting.getProcessId());
            }
        }
        flowMeetingMapper.delete(ids);
    }
}
