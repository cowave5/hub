/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.api.service.impl.SysAlarmServiceImpl;
import com.cowave.sys.admin.api.service.impl.SysLogServiceImpl;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysLog;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final SysAlarmServiceImpl sysAlarmService;

    private final SysLogServiceImpl sysLogService;

    @KafkaListener(topics = {"${application.oplog.topic.kafka:sys-oplog}"})
    public void recordOperation(ConsumerRecord<?, ?> message) {
        SysLog sysLog = JSON.parseObject(String.valueOf(message.value()), SysLog.class);
        sysLogService.accept(sysLog);
    }

    @KafkaListener(topics = {"${application.alarm.topic.kafka:sys-alarm}"})
    public void recordAlarm(ConsumerRecord<?, ?> message) {
        SysAlarm sysAlarm = JSON.parseObject(String.valueOf(message.value()), SysAlarm.class);
        sysAlarmService.handle(sysAlarm);
    }
}
