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
import com.cowave.hub.admin.domain.flow.Meeting;
import com.cowave.hub.admin.infra.flow.mapper.FlowMeetingMapper;
import com.cowave.hub.admin.service.flow.MeetingFlowService;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class MeetingFlowServiceImpl implements MeetingFlowService {

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
        identityService.setAuthenticatedUserId(Access.userId(String::valueOf));
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
