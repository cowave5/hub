/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
