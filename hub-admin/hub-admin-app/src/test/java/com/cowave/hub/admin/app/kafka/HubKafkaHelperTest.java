/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.hub.admin.app.SpringTest;
import com.cowave.hub.admin.domain.sys.HubOperation;
import com.cowave.hub.admin.domain.sys.dto.AlarmDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_USER;

/**
 * @author shanhuiming
 */
public class HubKafkaHelperTest extends SpringTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void send() {
        AlarmDto alarm = new AlarmDto();
        alarm.setAlarmCode("xxxxx");
        alarm.setSourceName("hub-admin");
        alarm.setAlarmContent(new HashMap<>());
        kafkaTemplate.send("hub-alarm", JSON.toJSONString(alarm));

        HubOperation hubOperation = new HubOperation();
        hubOperation.setOpType (SYSTEM_USER);
        hubOperation.setOpContent(new HashMap<>());
        kafkaTemplate.send("hub-oplog", JSON.toJSONString(hubOperation));
    }
}
