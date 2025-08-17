/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app;

import com.cowave.commons.framework.access.AccessAdvice;
import com.cowave.commons.framework.access.AccessProperties;
import com.cowave.commons.framework.access.filter.AccessIdGenerator;
import com.cowave.commons.framework.access.filter.TransactionIdSetter;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringTest {

    public static final PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres:13.1")
            .withUsername("postgres").withPassword("postgres").withDatabaseName("hub-admin").withExposedPorts(5432)
            .withCommand("postgres", "-c", "max_connections=1000")
            .withEnv("POSTGRES_MAX_CONNECTIONS", "1000");

    public static final GenericContainer<?> REDIS =
            new GenericContainer<>("redis:7.0").withExposedPorts(6379);

    public static final GenericContainer<?> ZK =
            new GenericContainer<>("zookeeper:3.8.1").withExposedPorts(2181);

    public static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1")).dependsOn(ZK);

    public static final MinIOContainer MINIO =
            new MinIOContainer("minio/minio:RELEASE.2023-09-04T19-57-37Z")
                    .withUserName("admin").withPassword("admin123").withExposedPorts(9000);

    static {
        PG.start();
        REDIS.start();
        ZK.start();
        KAFKA.start();
        MINIO.start();
    }

    protected MockMvc mockMvc;

    protected String accessToken;

    protected String refreshToken;

    @Autowired
    protected BearerTokenService bearerTokenService;

    @Autowired(required = false)
    protected TransactionIdSetter transactionIdSetter;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RedisHelper redisHelper;

    @Autowired
    protected AccessAdvice accessAdvice;

    @Autowired
    protected AccessProperties accessProperties;

    @Autowired
    protected AccessIdGenerator accessIdGenerator;

    @Autowired
    protected ApplicationProperties applicationProperties;

    @PostConstruct
    public void init() {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAuthType(UserType.SYS.getVal());
        userDetails.setUserId(6L);
        userDetails.setDeptId(1L);
        userDetails.setUsername("guanyu");
        userDetails.setUserNick("关羽");
        userDetails.setRoles(List.of(Permission.ROLE_ADMIN));
        redisHelper.putValue(applicationProperties.getName() + "user:" + userDetails.getUsername(), userDetails);

        bearerTokenService.assignAccessRefreshToken(userDetails);
        this.accessToken = userDetails.getAccessToken();
        this.refreshToken = userDetails.getRefreshToken();

        AccessUserDetails logoutUserDetails = AccessUserDetails.newUserDetails();
        logoutUserDetails.setAuthType(UserType.SYS.getVal());
        logoutUserDetails.setUserId(7L);
        logoutUserDetails.setDeptId(2L);
        logoutUserDetails.setUsername("zhangfei");
        logoutUserDetails.setUserNick("张飞");
        logoutUserDetails.setRoles(List.of(Permission.ROLE_ADMIN));
        redisHelper.putValue(applicationProperties.getName() + "user:" + logoutUserDetails.getUsername(), logoutUserDetails);
    }

    protected String requestId(){
        return "T" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    protected void mockGet(String url) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header("X-Request-ID", requestId())
                        .header("Authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    protected void mockDelete(String url) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .header("X-Request-ID", requestId())
                        .header("Authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    protected void mockPost(String url, String content) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("X-Request-ID", requestId())
                        .header("Authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    protected void mockPatch(String url, String content) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("X-Request-ID", requestId())
                        .header("Authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    protected void mockExport(String url, String content, String filePath) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("X-Request-ID", requestId())
                .header("Authorization", accessToken);
        if(content != null){
            requestBuilder.content(content);
        }
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(mvcResult -> {
                    try(FileOutputStream out = new FileOutputStream(filePath);
                        ByteArrayInputStream in = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())){
                        StreamUtils.copy(in, out);
                    }
                });
    }

    protected void mockImport(String url, MultiValueMap<String, String> params, String classPath) throws Exception {
        ClassPathResource resource = new ClassPathResource(classPath);
        try(InputStream inputStream = resource.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            StreamUtils.copy(inputStream, outputStream);
            MockMultipartFile file = new MockMultipartFile("file", "test.x",
                    MediaType.MULTIPART_FORM_DATA_VALUE, outputStream.toByteArray());
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart(url)
                    .file(file)
                    .header("X-Request-ID", requestId())
                    .header("Authorization", accessToken);
            if(params != null){
                requestBuilder.params(params);
            }
            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }
}
