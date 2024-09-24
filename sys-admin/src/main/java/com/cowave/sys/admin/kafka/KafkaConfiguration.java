/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author shanhuiming
 *
 */
@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic oplogTopic() {
        return new NewTopic("sys-oplog",1, (short) 1);
    }

    @Bean
    public NewTopic alarmTopic() {
        return new NewTopic("sys-alarm",1, (short) 1);
    }
}
