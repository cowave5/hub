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

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.cowave.sys.meter.core.plugin.IWorkspaceEnvPlugin;
import com.cowave.sys.meter.core.utils.CommandUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Hong
 **/
@Slf4j
public class GitProcessFactory {
    private static Boolean result;
    private static final String WIN_EXISTS_GIT = "where git";
    private static final String LINUX_EXISTS_GIT = "which git";
    private static final String DEFAULT_GIT_PROCESS = "JGit";
    private static final String SYSTEM_GIT_PROCESS = "SystemGit";

    public static GitProcess get(Map<String, Object> parameter, IWorkspaceEnvPlugin workspaceEnvPlugin) {
        String processType = (String) parameter.getOrDefault("gitProcessType", DEFAULT_GIT_PROCESS);
        if (SYSTEM_GIT_PROCESS.equalsIgnoreCase(processType) && GitProcessFactory.isGitExists()) {
            return new SystemGitProcess(workspaceEnvPlugin, parameter);
        } else {
            return new JGitProcess(workspaceEnvPlugin, parameter);
        }
    }

    public static boolean isGitExists() {
        if (result == null) {
            result = existsGit();
        }
        return result;
    }

    private static boolean existsGit() {
        String result;
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isWindows()) {
            result = CommandUtil.execCommand(WIN_EXISTS_GIT);
            return StrUtil.contains(result, ".exe");
        } else if (osInfo.isLinux() || osInfo.isMac()) {
            result = CommandUtil.execCommand(LINUX_EXISTS_GIT);
            return !StrUtil.containsAny(result, "no git", "not found");
        } else {
            return false;
        }
    }
}
