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

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.cowave.sys.meter.core.plugin.IDefaultPlugin;
import com.cowave.sys.meter.core.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author bwcx_jzy
 */
@Slf4j
@Plugin(name = "webhook")
public class WebhookPluginImpl implements IDefaultPlugin {

    @Override
    public Object execute(Object main, Map<String, Object> parameter) {
        String webhook = StrUtil.toStringOrNull(main);
        if (StrUtil.isEmpty(webhook)) {
            return null;
        }

        Object hookEvent = parameter.remove("HOOK_EVENT");
        if (hookEvent instanceof WebhookEvent webhookEvent) {
            log.debug("webhook event: [{}]{}", webhookEvent, webhook);
        }

        try {
            HttpRequest httpRequest = HttpUtil.createGet(webhook, true);
            httpRequest.form(parameter);
            try (HttpResponse execute = httpRequest.execute()) {
                String body = execute.body();
                log.info("{}" + CharPool.COLON + "{}", webhook, body);
                return body;
            }
        } catch (Exception e) {
            log.error("webHook调用失败", e);
            return "WebHooks error:" + e.getMessage();
        }
    }
}
