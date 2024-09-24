/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.eureka.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 *
 * @author shanhm1991
 *
 */
@Configuration
public class HealthConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthConfiguration.class);

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");              // 任意地址
        config.addAllowedHeader("*");                     // 任意请求头
        config.addAllowedMethod("*");                     // 任意请求方法
        config.setMaxAge(1800L);                          // 有效期 1800秒
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 任意url
        return new CorsFilter(source);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String infoPath = "/META-INF/info.yml";
        Resource infoResource = new ClassPathResource(infoPath);
        if(!infoResource.exists()){
            return;
        }

        try{
            LOGGER.info("prepare to load: META-INF/info.yml");
            List<PropertySource<?>> list = new YamlPropertySourceLoader().load(infoPath, infoResource);
            for(PropertySource<?> source : list){
                environment.getPropertySources().addLast(source);
            }
        }catch (Exception e){
            LOGGER.error("failed to load: META-INF/info.yml", e);
            System.exit(-1);
        }
    }
}
