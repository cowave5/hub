package com.cowave.sys.job.client.configuration;

import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.sys.job.client.JobContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({ApplicationProperties.class, JobProperties.class})
public class JobConfiguration {

    @Bean
    public JobContext jobContext(ApplicationProperties applicationProperties, JobProperties jobProperties) {
		String clientName = jobProperties.clientName();
		if(StringUtils.isBlank(clientName)){
			clientName = applicationProperties.getName();
		}
		if(StringUtils.isBlank(clientName)){
			clientName = "unknown";
		}

		JobContext jobContext = new JobContext();
		jobContext.setClinetName(clientName);
        jobContext.setClientIp(jobProperties.clientIp());
		jobContext.setClientPort(jobProperties.clientPort());
        jobContext.setServerList(jobProperties.serverAddress());
		return jobContext;
	}
}
