/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base;

import com.alibaba.fastjson.JSON;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.AccessExceptionHandler;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysAlarmDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求告警
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysExceptionHandler implements AccessExceptionHandler {

    private final ApplicationProperties applicationProperties;

    private final SysAlarmDtoMapper sysAlarmMapper;

    @Override
    public void handler(Exception e, int status, Response<Void> response) {
        Map<String, Object> content = new HashMap<>();
        content.put("requestId", Access.accessId());
        content.put("requestUrl", Access.accessUrl());
        content.put("requestParam", Access.getRequestParam());
        content.put("responseCode", response.getCode());
        content.put("responseMsg", response.getMsg());
        content.put("responseData", response.getData());

        SysAlarmDto sysAlarm = new SysAlarmDto();
        String group = applicationProperties.getName();
        String type = "access_failed";

        String param = "";
        if(Access.getRequestParam() != null){
            param = JSON.toJSONString(Access.getRequestParam());
        }
        String md5 = DigestUtils.md5Hex(Access.accessUrl() + param);
        sysAlarm.recordAlarm(md5, group, type, Access.accessUrl(), content);
        sysAlarm.setAlarmLevel(3);
        if(sysAlarmMapper.alarmIncrease(sysAlarm) == 0) {
            sysAlarmMapper.insert(sysAlarm);
        }
    }
}
