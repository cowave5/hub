/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author shanhuiming
 */
@Slf4j
@Component
public class TestTask {

    public void test1() throws InterruptedException {
        int cost = RandomUtils.nextInt(100, 1000);
        Thread.sleep(cost);
        log.info("执行：test1 耗时：{}ms", cost);
    }

    public void test2(String param) throws InterruptedException {
        int cost = RandomUtils.nextInt(100, 1000);
        Thread.sleep(cost);
        log.info("执行：test2 耗时：{}ms 参数：{}", cost, param);
    }

    public void test3(String s, Boolean b, Long l, Double d, Integer i) throws InterruptedException {
        int cost = RandomUtils.nextInt(100, 1000);
        Thread.sleep(cost);
        log.info("执行：test3 耗时：{}ms 参数：{} {} {} {} {}", cost, s, b, l, d, i);
    }
}
