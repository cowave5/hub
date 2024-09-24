/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin;

import com.cowave.commons.framework.access.AccessAdvice;
import com.cowave.commons.framework.filter.access.AccessIdGenerator;
import com.cowave.commons.framework.filter.access.TransactionIdSetter;
import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.filter.security.Permission;
import com.cowave.commons.framework.filter.security.TokenService;
import com.cowave.commons.framework.helper.MessageHelper;
import com.cowave.commons.framework.support.redis.RedisHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 *
 * @author shanhuiming
 *
 */
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringTest {

    public static final PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres:13.1")
            .withUsername("postgres").withPassword("postgres").withDatabaseName("sys-admin").withExposedPorts(5432)
            .withCommand("postgres", "-c", "max_connections=1000")
            .withEnv("POSTGRES_MAX_CONNECTIONS", "1000");

    public static final GenericContainer<?> REDIS = new GenericContainer<>("redis:7.0").withExposedPorts(6379);

    public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    public static final MinIOContainer MINIO = new MinIOContainer("minio/minio:RELEASE.2023-09-04T19-57-37Z")
            .withUserName("admin").withPassword("admin123").withExposedPorts(9000);

    static {
        PG.start();
        REDIS.start();
        KAFKA.start();
        MINIO.start();
    }

    protected MockMvc mockMvc;

    protected String accessToken;

    protected String refreshToken;

    @Autowired
    protected TokenService tokenService;

    @Autowired(required = false)
    protected TransactionIdSetter transactionIdSetter;

    @Autowired
    protected MessageHelper messageHelper;

    @Autowired
    protected AccessAdvice accessAdvice;

    @Autowired
    protected RedisHelper redisHelper;

    @Autowired
    protected AccessIdGenerator accessIdGenerator;

    @PostConstruct
    public void init() {
        AccessToken loginToken = AccessToken.newToken();
        loginToken.setType(AccessToken.TYPE_USER);
        loginToken.setUserId(6L);
        loginToken.setDeptId(1L);
        loginToken.setUsername("guanyu");
        loginToken.setUserNick("关羽");
        loginToken.setRoles(List.of(Permission.ROLE_ADMIN));
        redisHelper.putValue(AccessToken.KEY + "user:" + loginToken.getUsername(), loginToken);

        tokenService.assignToken(loginToken);
        this.accessToken = loginToken.getAccessToken();
        this.refreshToken = loginToken.getRefreshToken();

        AccessToken logoutToken = AccessToken.newToken();
        logoutToken.setType(AccessToken.TYPE_USER);
        logoutToken.setUserId(7L);
        logoutToken.setDeptId(2L);
        logoutToken.setUsername("zhangfei");
        logoutToken.setUserNick("张飞");
        logoutToken.setRoles(List.of(Permission.ROLE_ADMIN));
        redisHelper.putValue(AccessToken.KEY + "user:" + logoutToken.getUsername(), logoutToken);
    }

    protected String requestId(){
        return "T-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    protected void mockGet(String url) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header("requestId", requestId())
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
                        .header("requestId", requestId())
                        .header("Authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    protected void mockExport(String url, String content, String filePath) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("requestId", requestId())
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
                    .header("requestId", requestId())
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
