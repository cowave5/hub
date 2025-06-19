package com.cowave.sys.job.app.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuxueli/shanhuiming
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.raft")
public class RaftConfiguration {

    private boolean enable;

    private String nodes;

    private String currentNode;
}
