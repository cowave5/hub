/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.gateway.filter;

import com.cowave.commons.client.http.asserts.I18Messages;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.client.http.response.ResponseCode;
import com.cowave.commons.framework.access.AccessProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.tools.NetUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.commons.client.http.constants.HttpHeader.*;
import static com.cowave.commons.framework.access.security.BearerTokenService.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private final AccessProperties accessProperties;
    private final ObjectMapper objectMapper;
    private final RedisHelper redisHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();

        // url白名单
        String accessUrl = httpRequest.getURI().getPath();
        String[] ignoreUrls = accessProperties.ignoreUrls();
        if (ignoreUrls != null && Arrays.stream(ignoreUrls).anyMatch(p -> PATH_MATCHER.match(p, accessUrl))) {
            return chain.filter(exchange);
        }

        // 国际化
        String language = httpRequest.getHeaders().getFirst(Accept_Language);
        I18Messages.setLanguage(language);

        // 获取token
        String token = httpRequest.getHeaders().getFirst(Authorization);
        if (StringUtils.isBlank(token)) {
            return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.no");
        }
        if(token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        // 校验token
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(accessProperties.accessSecret()).parseClaimsJws(token).getBody();
        }catch(ExpiredJwtException e) {
            return writeResponse(exchange.getResponse(), INVALID_TOKEN, "frame.auth.expired");
        }catch(Exception e) {
            return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.invalid");
        }

        String tokenType = (String) claims.get(CLAIM_TYPE);
        String accessId = (String) claims.get(CLAIM_ACCESS_ID);

        String accessIp = getAccessIp(httpRequest);
        if ("api".equals(tokenType)) {
            String accessKey = "sys-admin:token:api:" + accessId;
            // 是否已注销
            List<NetUtils.IpMask> ipRules = redisHelper.getValue(accessKey);
            if(ipRules == null){
                return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.denied");
            }

            // 校验Ip段
            boolean isIpAllowed = false;
            if (CollectionUtils.isEmpty(ipRules)) {
                isIpAllowed = true;
            } else {
                for (NetUtils.IpMask ipMask : ipRules) {
                    if (ipMask.contains(accessIp)) {
                        isIpAllowed = true;
                        break;
                    }
                }
            }
            if(!isIpAllowed){
                return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.ip");
            }

            Map<String, Object> accessInfo = Map.of(
                    "ip", accessIp,
                    "url", accessUrl,
                    "time", new Date());
            redisHelper.putExpire("sys-admin:token:api:current:" + accessId, accessInfo, 15, TimeUnit.MINUTES);
        } else {
            // IP变化要求重新刷一下accessToken
            String userIp = (String) claims.get(CLAIM_ACCESS_IP);
            String tokenConflict = (String) claims.get(CLAIM_CONFLICT);
            if ("Y".equals(tokenConflict) && !Objects.equals(accessIp, userIp)) {
                return writeResponse(exchange.getResponse(), INVALID_TOKEN, "frame.auth.ipchanged");
            }

            // 是否已注销
            String accessKey = "sys-admin:token:access:" + accessId;
            if(!redisHelper.existKey(accessKey)){
                return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.denied");
            }
        }

        // 获取载荷部分
        String[] tokenParts = token.split("\\.");
        String tokenPayload = tokenParts[1];
        exchange.mutate().request(httpRequest.mutate().header(X_User_Payload, tokenPayload).build()).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String getAccessIp(ServerHttpRequest httpRequest) {
        String ip = getFirstNonUnknown(
                httpRequest.getHeaders().getFirst(X_Real_IP),
                httpRequest.getHeaders().getFirst(X_Forwarded_For),
                httpRequest.getHeaders().getFirst(Proxy_Client_IP),
                httpRequest.getHeaders().getFirst(WL_Proxy_Client_IP)
        );

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getRemoteAddress() != null
                    ? httpRequest.getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    private String getFirstNonUnknown(String... values) {
        for (String val : values) {
            if (val != null && !val.isEmpty() && !"unknown".equalsIgnoreCase(val)) {
                if (val.contains(",")) {
                    return val.split(",")[0].trim();
                }
                return val;
            }
        }
        return null;
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, ResponseCode responseCode, String messageKey) {
        // int httpStatus = responseCode.getStatus();
        response.setStatusCode(HttpStatus.valueOf(SUCCESS.getStatus()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(Response.msg(responseCode, I18Messages.msg(messageKey)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
