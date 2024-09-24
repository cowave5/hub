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
import com.cowave.sys.admin.SpringTest;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

/**
 *
 * @author shanhuiming
 *
 */
public class KafkaConsumerTest extends SpringTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void send() {
        SysAlarm sysAlarm = new SysAlarm();
        sysAlarm.setAlarmCode("xxxxx");
        sysAlarm.setSourceName("sys-admin");
        sysAlarm.setAlarmContent(new HashMap<>());
        kafkaTemplate.send("sys-alarm", JSON.toJSONString(sysAlarm));

        SysLog sysLog = new SysLog();
        sysLog.setTypeCode("sys-user");
        sysLog.setLogContent(new HashMap<>());
        kafkaTemplate.send("sys-oplog", JSON.toJSONString(sysLog));
    }
}
