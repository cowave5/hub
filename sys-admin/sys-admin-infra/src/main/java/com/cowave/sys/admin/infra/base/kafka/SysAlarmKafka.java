/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysAlarmDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAlarmKafka {

    private final SysAlarmDtoMapper sysAlarmMapper;

    @KafkaListener(topics = {"${spring.access.alarm.kafka-topic:sys-alarm}"})
    public void recordAlarm(ConsumerRecord<?, ?> message) {
        SysAlarmDto sysAlarm = JSON.parseObject(String.valueOf(message.value()), SysAlarmDto.class);
        if(sysAlarmMapper.alarmIncrease(sysAlarm) == 0) {
            sysAlarmMapper.insert(sysAlarm);
        }
    }
}
