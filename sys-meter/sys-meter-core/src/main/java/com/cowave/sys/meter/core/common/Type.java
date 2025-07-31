package com.cowave.sys.meter.core.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;

import java.util.function.Function;

/**
 * @author bwcx_jzy
 */
public enum Type {
    Agent("org.dromara.jpom.JpomAgentApplication", (remoteVersion) -> {
        String jpomRemoteVersionAuth = SystemUtil.get("JPOM_REMOTE_VERSION_AUTH", "");
        if (StrUtil.isNotEmpty(jpomRemoteVersionAuth)) {
            RemoteVersion.RemoteVersionAuth auth = remoteVersion.getAuth();
            if (auth != null && StrUtil.isNotEmpty(auth.getAgentUrl())) {
                String agentUrl = auth.getAgentUrl();
                return StrUtil.replace(agentUrl, "{token}", jpomRemoteVersionAuth);
            }
        }

        return remoteVersion.getAgentUrl();
    }, "JPOM_AGENT_APPLICATION"),

    Server("org.dromara.jpom.JpomServerApplication", (remoteVersion) -> {
        String jpomRemoteVersionAuth = SystemUtil.get("JPOM_REMOTE_VERSION_AUTH", "");
        if (StrUtil.isNotEmpty(jpomRemoteVersionAuth)) {
            RemoteVersion.RemoteVersionAuth auth = remoteVersion.getAuth();
            if (auth != null && StrUtil.isNotEmpty(auth.getServerUrl())) {
                String serverUrl = auth.getServerUrl();
                return StrUtil.replace(serverUrl, "{token}", jpomRemoteVersionAuth);
            }
        }
        return remoteVersion.getServerUrl();
    }, "JPOM_SERVER_APPLICATION");

    private final Function<RemoteVersion, String> remoteUrl;

    private final String applicationClass;

    private final String tag;

    private Type(String applicationClass, Function<RemoteVersion, String> remoteUrl, String tag) {
        this.applicationClass = applicationClass;
        this.remoteUrl = remoteUrl;
        this.tag = tag;
    }

    public String getRemoteUrl(RemoteVersion remoteVersion) {
        return (String)this.remoteUrl.apply(remoteVersion);
    }

    public Function<RemoteVersion, String> getRemoteUrl() {
        return this.remoteUrl;
    }

    public String getApplicationClass() {
        return this.applicationClass;
    }

    public String getTag() {
        return this.tag;
    }
}
