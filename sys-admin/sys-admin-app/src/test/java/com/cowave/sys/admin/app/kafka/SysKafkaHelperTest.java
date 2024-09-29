/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.app.SpringTest;
import com.cowave.sys.admin.domain.base.SysOperation;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

/**
 * @author shanhuiming
 */
public class SysKafkaHelperTest extends SpringTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void send() {
        SysAlarmDto sysAlarm = new SysAlarmDto();
        sysAlarm.setAlarmCode("xxxxx");
        sysAlarm.setSourceName("sys-admin");
        sysAlarm.setAlarmContent(new HashMap<>());
        kafkaTemplate.send("sys-alarm", JSON.toJSONString(sysAlarm));

        SysOperation sysOperation = new SysOperation();
        sysOperation.setOpType ("sys-user");
        sysOperation.setOpContent(new HashMap<>());
        kafkaTemplate.send("sys-oplog", JSON.toJSONString(sysOperation));
    }
}
