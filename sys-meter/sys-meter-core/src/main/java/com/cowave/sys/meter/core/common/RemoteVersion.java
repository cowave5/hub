package com.cowave.sys.meter.core.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.*;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author bwcx_jzy
 */
public class RemoteVersion {
    private static final Logger log = LoggerFactory.getLogger(RemoteVersion.class);
    private static final String DEFAULT_URL = "https://jpom.top/docs/release-versions.json";
    private static final String BETA_URL = "https://jpom.top/docs/beta-versions.json";
    private static String remoteVersionUrl;
    private static final int CHECK_INTERVAL = 24;
    private String tagName;
    private String agentUrl;
    private String serverUrl;
    private String changelogUrl;
    private String changelog;
    private Long lastTime;
    private Boolean upgrade;
    private Boolean beta;
    private String downloadSource;
    private RemoteVersionAuth auth;

    public String getDownloadSource() {
        String jpomRemoteVersionAuth = SystemUtil.get("JPOM_REMOTE_VERSION_AUTH", "");
        if (StrUtil.isNotEmpty(jpomRemoteVersionAuth)) {
            RemoteVersionAuth auth = this.getAuth();
            if (StrUtil.isNotEmpty(auth.getDownloadSource())) {
                return auth.getDownloadSource();
            }
        }

        return this.downloadSource;
    }

    public static void setRemoteVersionUrl(String remoteVersionUrl) {
        RemoteVersion.remoteVersionUrl = remoteVersionUrl;
    }

    private static String loadDefaultUrl() {
        boolean beta = betaRelease();
        return beta ? "https://jpom.top/docs/beta-versions.json" : "https://jpom.top/docs/release-versions.json";
    }

    public static boolean betaRelease() {
        String betaRelease = SystemPropsUtil.get("JOIN_JPOM_BETA_RELEASE", "");
        return Convert.toBool(betaRelease, false);
    }

    public static void changeBetaRelease(String beta) {
        SystemPropsUtil.set("JOIN_JPOM_BETA_RELEASE", beta);
    }

    public static RemoteVersion loadRemoteInfo() {
        String body = "";

        try {
            String remoteVersionUrl = StrUtil.emptyToDefault(RemoteVersion.remoteVersionUrl, loadDefaultUrl());
            remoteVersionUrl = Validator.isUrl(remoteVersionUrl) ? remoteVersionUrl : loadDefaultUrl();
            RemoteVersion remoteVersion = loadTransitUrl(remoteVersionUrl);
            if (remoteVersion != null && !StrUtil.isEmpty(remoteVersion.getTagName())) {
                cacheLoadTime(remoteVersion);
                return remoteVersion;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("获取远程版本信息失败:{} {}", e.getMessage(), body);
            return null;
        }
    }

    private static RemoteVersion loadTransitUrl(String remoteVersionUrl) {
        String body = "";

        try {
            log.debug("use remote version url: {}", remoteVersionUrl);
            HttpRequest request = HttpUtil.createGet(remoteVersionUrl, true);
            request.timeout(10000);

            try (HttpResponse execute = request.execute()) {
                body = execute.body();
            }

            JSONObject jsonObject = JSONObject.parseObject(body);
            RemoteVersion remoteVersion = (RemoteVersion)jsonObject.to(RemoteVersion.class, new JSONReader.Feature[0]);
            if (StrUtil.isAllNotEmpty(new CharSequence[]{remoteVersion.getTagName(), remoteVersion.getAgentUrl(), remoteVersion.getServerUrl(), remoteVersion.getServerUrl()})) {
                return remoteVersion;
            } else {
                String jumpUrl = jsonObject.getString("url");
                return StrUtil.isEmpty(jumpUrl) ? null : loadTransitUrl(jumpUrl);
            }
        } catch (Exception e) {
            log.warn("获取远程版本信息失败:{} {}", e.getMessage(), body);
            return null;
        }
    }

    private static void cacheLoadTime(RemoteVersion remoteVersion) {
        remoteVersion = (RemoteVersion) ObjectUtil.defaultIfNull(remoteVersion, new RemoteVersion());
        remoteVersion.setLastTime(SystemClock.now());
        boolean isDebug = BooleanUtil.toBoolean(SystemUtil.get("JPOM_IS_DEBUG", ""));
        String jpomType = SystemUtil.get("JPOM_TYPE", "");
        Type type = (Type) EnumUtil.fromStringQuietly(Type.class, jpomType);
        Assert.notNull(type, "没有配置正确的环境变量", new Object[0]);
        if (!isDebug) {
            String version = SystemUtil.get("JPOM_VERSION", "");
            String tagName = remoteVersion.getTagName();
            tagName = StrUtil.removePrefixIgnoreCase(tagName, "v");
            remoteVersion.setUpgrade(StrUtil.compareVersion(version, tagName) < 0);
        } else {
            remoteVersion.setUpgrade(false);
        }

        String remoteUrl = type.getRemoteUrl(remoteVersion);
        if (StrUtil.isEmpty(remoteUrl)) {
            remoteVersion.setUpgrade(false);
        }

        String changelogUrl = remoteVersion.getChangelogUrl();
        if (StrUtil.isNotEmpty(changelogUrl)) {
            try (HttpResponse execute = HttpUtil.createGet(changelogUrl, true).execute()) {
                String body = execute.body();
                remoteVersion.setChangelog(body);
            }
        }

        File file = getFile();
        FileUtil.writeUtf8String(remoteVersion.toString(), file);
    }

    public static RemoteVersion cacheInfo() {
        File file = getFile();
        if (!FileUtil.isFile(file)) {
            return null;
        } else {
            RemoteVersion remoteVersion = null;
            String fileStr = "";

            try {
                fileStr = FileUtil.readUtf8String(file);
                if (StrUtil.isEmpty(fileStr)) {
                    return null;
                }

                remoteVersion = (RemoteVersion)JSONObject.parseObject(fileStr).to(RemoteVersion.class, new JSONReader.Feature[0]);
            } catch (Exception e) {
                log.warn("解析远程版本信息失败:{} {}", e.getMessage(), fileStr);
            }

            Long lastTime = remoteVersion == null ? 0L : remoteVersion.getLastTime();
            lastTime = (Long)ObjectUtil.defaultIfNull(lastTime, 0L);
            long interval = SystemClock.now() - lastTime;
            return interval >= TimeUnit.HOURS.toMillis(24L) ? null : remoteVersion;
        }
    }

    private static File getFile() {
        String cacheFile = SystemUtil.get("JPOM_REMOTE_VERSION_CACHE_FILE", "");
        Assert.state(StrUtil.isNotEmpty(cacheFile), "没有配置正确的环境变量", new Object[0]);
        return FileUtil.file(cacheFile);
    }

    public String toString() {
        return JSONObject.toJSONString(this, new JSONWriter.Feature[0]);
    }

    public RemoteVersion() {
    }

    public String getTagName() {
        return this.tagName;
    }

    public String getAgentUrl() {
        return this.agentUrl;
    }

    public String getServerUrl() {
        return this.serverUrl;
    }

    public String getChangelogUrl() {
        return this.changelogUrl;
    }

    public String getChangelog() {
        return this.changelog;
    }

    public Long getLastTime() {
        return this.lastTime;
    }

    public Boolean getUpgrade() {
        return this.upgrade;
    }

    public Boolean getBeta() {
        return this.beta;
    }

    public RemoteVersionAuth getAuth() {
        return this.auth;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setAgentUrl(String agentUrl) {
        this.agentUrl = agentUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setChangelogUrl(String changelogUrl) {
        this.changelogUrl = changelogUrl;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public void setUpgrade(Boolean upgrade) {
        this.upgrade = upgrade;
    }

    public void setBeta(Boolean beta) {
        this.beta = beta;
    }

    public void setDownloadSource(String downloadSource) {
        this.downloadSource = downloadSource;
    }

    public void setAuth(RemoteVersionAuth auth) {
        this.auth = auth;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RemoteVersion)) {
            return false;
        } else {
            RemoteVersion other = (RemoteVersion)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$lastTime = this.getLastTime();
                Object other$lastTime = other.getLastTime();
                if (this$lastTime == null) {
                    if (other$lastTime != null) {
                        return false;
                    }
                } else if (!this$lastTime.equals(other$lastTime)) {
                    return false;
                }

                Object this$upgrade = this.getUpgrade();
                Object other$upgrade = other.getUpgrade();
                if (this$upgrade == null) {
                    if (other$upgrade != null) {
                        return false;
                    }
                } else if (!this$upgrade.equals(other$upgrade)) {
                    return false;
                }

                Object this$beta = this.getBeta();
                Object other$beta = other.getBeta();
                if (this$beta == null) {
                    if (other$beta != null) {
                        return false;
                    }
                } else if (!this$beta.equals(other$beta)) {
                    return false;
                }

                Object this$tagName = this.getTagName();
                Object other$tagName = other.getTagName();
                if (this$tagName == null) {
                    if (other$tagName != null) {
                        return false;
                    }
                } else if (!this$tagName.equals(other$tagName)) {
                    return false;
                }

                Object this$agentUrl = this.getAgentUrl();
                Object other$agentUrl = other.getAgentUrl();
                if (this$agentUrl == null) {
                    if (other$agentUrl != null) {
                        return false;
                    }
                } else if (!this$agentUrl.equals(other$agentUrl)) {
                    return false;
                }

                Object this$serverUrl = this.getServerUrl();
                Object other$serverUrl = other.getServerUrl();
                if (this$serverUrl == null) {
                    if (other$serverUrl != null) {
                        return false;
                    }
                } else if (!this$serverUrl.equals(other$serverUrl)) {
                    return false;
                }

                Object this$changelogUrl = this.getChangelogUrl();
                Object other$changelogUrl = other.getChangelogUrl();
                if (this$changelogUrl == null) {
                    if (other$changelogUrl != null) {
                        return false;
                    }
                } else if (!this$changelogUrl.equals(other$changelogUrl)) {
                    return false;
                }

                Object this$changelog = this.getChangelog();
                Object other$changelog = other.getChangelog();
                if (this$changelog == null) {
                    if (other$changelog != null) {
                        return false;
                    }
                } else if (!this$changelog.equals(other$changelog)) {
                    return false;
                }

                Object this$downloadSource = this.getDownloadSource();
                Object other$downloadSource = other.getDownloadSource();
                if (this$downloadSource == null) {
                    if (other$downloadSource != null) {
                        return false;
                    }
                } else if (!this$downloadSource.equals(other$downloadSource)) {
                    return false;
                }

                Object this$auth = this.getAuth();
                Object other$auth = other.getAuth();
                if (this$auth == null) {
                    if (other$auth != null) {
                        return false;
                    }
                } else if (!this$auth.equals(other$auth)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RemoteVersion;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $lastTime = this.getLastTime();
        result = result * 59 + ($lastTime == null ? 43 : $lastTime.hashCode());
        Object $upgrade = this.getUpgrade();
        result = result * 59 + ($upgrade == null ? 43 : $upgrade.hashCode());
        Object $beta = this.getBeta();
        result = result * 59 + ($beta == null ? 43 : $beta.hashCode());
        Object $tagName = this.getTagName();
        result = result * 59 + ($tagName == null ? 43 : $tagName.hashCode());
        Object $agentUrl = this.getAgentUrl();
        result = result * 59 + ($agentUrl == null ? 43 : $agentUrl.hashCode());
        Object $serverUrl = this.getServerUrl();
        result = result * 59 + ($serverUrl == null ? 43 : $serverUrl.hashCode());
        Object $changelogUrl = this.getChangelogUrl();
        result = result * 59 + ($changelogUrl == null ? 43 : $changelogUrl.hashCode());
        Object $changelog = this.getChangelog();
        result = result * 59 + ($changelog == null ? 43 : $changelog.hashCode());
        Object $downloadSource = this.getDownloadSource();
        result = result * 59 + ($downloadSource == null ? 43 : $downloadSource.hashCode());
        Object $auth = this.getAuth();
        result = result * 59 + ($auth == null ? 43 : $auth.hashCode());
        return result;
    }

    public static class RemoteVersionAuth {
        private String downloadSource;
        private String agentUrl;
        private String serverUrl;

        public RemoteVersionAuth() {
        }

        public String getDownloadSource() {
            return this.downloadSource;
        }

        public String getAgentUrl() {
            return this.agentUrl;
        }

        public String getServerUrl() {
            return this.serverUrl;
        }

        public void setDownloadSource(String downloadSource) {
            this.downloadSource = downloadSource;
        }

        public void setAgentUrl(String agentUrl) {
            this.agentUrl = agentUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof RemoteVersionAuth)) {
                return false;
            } else {
                RemoteVersionAuth other = (RemoteVersionAuth)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$downloadSource = this.getDownloadSource();
                    Object other$downloadSource = other.getDownloadSource();
                    if (this$downloadSource == null) {
                        if (other$downloadSource != null) {
                            return false;
                        }
                    } else if (!this$downloadSource.equals(other$downloadSource)) {
                        return false;
                    }

                    Object this$agentUrl = this.getAgentUrl();
                    Object other$agentUrl = other.getAgentUrl();
                    if (this$agentUrl == null) {
                        if (other$agentUrl != null) {
                            return false;
                        }
                    } else if (!this$agentUrl.equals(other$agentUrl)) {
                        return false;
                    }

                    Object this$serverUrl = this.getServerUrl();
                    Object other$serverUrl = other.getServerUrl();
                    if (this$serverUrl == null) {
                        if (other$serverUrl != null) {
                            return false;
                        }
                    } else if (!this$serverUrl.equals(other$serverUrl)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof RemoteVersionAuth;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Object $downloadSource = this.getDownloadSource();
            result = result * 59 + ($downloadSource == null ? 43 : $downloadSource.hashCode());
            Object $agentUrl = this.getAgentUrl();
            result = result * 59 + ($agentUrl == null ? 43 : $agentUrl.hashCode());
            Object $serverUrl = this.getServerUrl();
            result = result * 59 + ($serverUrl == null ? 43 : $serverUrl.hashCode());
            return result;
        }

        public String toString() {
            return "RemoteVersion.RemoteVersionAuth(downloadSource=" + this.getDownloadSource() + ", agentUrl=" + this.getAgentUrl() + ", serverUrl=" + this.getServerUrl() + ")";
        }
    }
}
