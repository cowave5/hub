/*
 * Copyright (c) 2019 Of Him Code Technology Studio
 * Jpom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * 			http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package com.cowave.sys.meter.plugin.webhook;

/**
 * @author bwcx_jzy
 */
public enum WebhookEvent {

    /**
     * 构建
     */
    BUILD,

    /**
     * 项目
     */
    PROJECT,

    /**
     * 监控
     */
    MONITOR,

    /**
     * 分发
     */
    DISTRIBUTE,
}
