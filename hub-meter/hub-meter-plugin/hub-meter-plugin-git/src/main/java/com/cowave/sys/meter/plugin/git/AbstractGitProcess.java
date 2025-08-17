/*
 * Copyright (c) 2019 Of Him Code Technology Studio
 * Jpom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * 			http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package com.cowave.sys.meter.plugin.git;

import cn.hutool.core.util.URLUtil;
import com.cowave.sys.meter.core.plugin.IWorkspaceEnvPlugin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Hong
 */
@Slf4j
public abstract class AbstractGitProcess implements GitProcess {

    /**
     * 执行参数
     */
    protected final Map<String, Object> parameter;

    /**
     * 执行插件
     */
    private final IWorkspaceEnvPlugin workspaceEnvPlugin;
    protected final String remoteUrl;
    protected final String username;
    protected final String password;
    protected final int protocol;
    protected final String branchName;
    protected final String tagName;
    protected final Integer depth;
    protected final Integer timeout;
    protected final File savePath;
    protected final File rsaFile;
    protected final Boolean strictlyEnforce;
    protected final PrintWriter printWriter;
    protected final Integer progressRatio;

    protected AbstractGitProcess(IWorkspaceEnvPlugin workspaceEnvPlugin, Map<String, Object> parameter) {
        this.workspaceEnvPlugin = workspaceEnvPlugin;
        this.parameter = decryptParameter(parameter);
        this.remoteUrl = (String) parameter.get("url");
        this.username = URLUtil.encodeAll((String) parameter.getOrDefault("username", ""));
        this.password = URLUtil.encodeAll((String) parameter.getOrDefault("password", ""));
        this.protocol = (int) parameter.getOrDefault("protocol", 0);
        this.branchName = (String) parameter.get("branchName");
        this.tagName = (String) parameter.get("tagName");
        this.depth = (Integer) parameter.get("depth");
        this.timeout = (Integer) parameter.get("timeout");
        this.savePath = (File) parameter.get("savePath");
        this.rsaFile = (File) parameter.get("rsaFile");
        this.strictlyEnforce = (Boolean) parameter.get("strictlyEnforce");
        this.printWriter = (PrintWriter) parameter.get("logWriter");
        this.progressRatio = (Integer) parameter.get("reduceProgressRatio");
    }

    protected Map<String, Object> decryptParameter(Map<String, Object> parameter) {
//        try {
//            parameter.put("password", workspaceEnvPlugin.convertRefEnvValue(parameter, "password"));
//            parameter.put("username", workspaceEnvPlugin.convertRefEnvValue(parameter, "username"));
//        } catch (Exception e) {
//            log.error("解密参数失败", e);
//        }
        return parameter;
    }
}
