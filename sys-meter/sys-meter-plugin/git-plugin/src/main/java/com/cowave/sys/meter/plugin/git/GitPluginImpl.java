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

import com.cowave.sys.meter.core.plugin.IWorkspaceEnvPlugin;
import com.cowave.sys.meter.core.plugin.Plugin;
import org.eclipse.jgit.api.Git;

import java.util.Map;

/**
 * @author Hong
 */
@Plugin(name = "git")
public class GitPluginImpl implements IWorkspaceEnvPlugin {

    @Override
    public Object execute(Object main, Map<String, Object> parameter) throws Exception {
        GitProcess gitProcess = GitProcessFactory.get(parameter, this);
        String type = main.toString();
        return switch (type) {
            case "listBranchAndTag" -> gitProcess.listBranchAndTag();
            case "pullBranch" -> gitProcess.pullBranch();
            case "pullTag" -> gitProcess.pullTag();
            case "isGitExists" -> GitProcessFactory.isGitExists();
            default -> null;
        };
    }

    @Override
    public void close() {
        Git.shutdown();
    }
}
