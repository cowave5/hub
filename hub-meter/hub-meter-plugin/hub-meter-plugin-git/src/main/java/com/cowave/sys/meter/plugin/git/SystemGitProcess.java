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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import com.cowave.sys.meter.core.plugin.IWorkspaceEnvPlugin;
import com.cowave.sys.meter.core.utils.CommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Constants;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hong
 */
@Slf4j
public class SystemGitProcess extends AbstractGitProcess {

    protected SystemGitProcess(IWorkspaceEnvPlugin workspaceEnvPlugin, Map<String, Object> parameter) {
        super(workspaceEnvPlugin, parameter);
        PrintWriter printWriter = (PrintWriter) parameter.get("logWriter");
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    // ssh-agent bash -c 'ssh-add <path-to-private-key>; git clone git@<host>:<username>/<repo-name>.git'
    // ssh-agent bash -c 'ssh-add /path/to/private_key && ssh -o StrictHostKeyChecking=yes git@github.com && git clone git@github.com:user/repo.git'

    private String gitUrl() throws MalformedURLException {
        if (protocol == 0) {
            // 已经配置
            if (StrUtil.contains(remoteUrl, "@")) {
                return remoteUrl;
            }
            // 拼接账号密码
            return getUrl(username, password, remoteUrl).toString();
        }
        return remoteUrl;
    }

    private static URL getUrl(String username, String password, String url) throws MalformedURLException {
        String userInfo = username + ":" + password;
        return new URL(null, url, new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) {
                return null;
            }

            @Override
            protected void setURL(URL u, String protocol, String host, int port, String authority, String userInfo2, String path, String query, String ref) {
                super.setURL(u, protocol, host, port, StrUtil.format("{}@{}", userInfo, authority), userInfo, path, query, ref);
            }
        });
    }

    private String warpSsh(String command) {
        if (protocol == 0) {
            return command;
        } else if (protocol == 1) {
            // TODO 需要实现本地 git ssh 指定证书拉取
            if (FileUtil.isFile(rsaFile)) {
                throw new IllegalStateException("暂时不支持本地 git 指定证书拉取代码");
            }
            // 默认的方式去执行
            return command;
        } else {
            throw new IllegalArgumentException("不支持的 protocol " + protocol);
        }
    }

    @Override
    public Tuple listBranchAndTag() throws Exception {
        String command = StrUtil.format("git ls-remote {}", this.gitUrl());
        command = this.warpSsh(command);
        String result = CommandUtil.execCommand(command);

        List<String> branches = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        List<String> list = StrUtil.splitTrim(result, StrUtil.LF);
        for (String branch : list) {
            List<String> list1 = StrUtil.splitTrim(branch, StrUtil.TAB);
            String last = CollUtil.getLast(list1);
            if (StrUtil.startWith(last, Constants.R_HEADS)) {
                branches.add(StrUtil.removePrefix(last, Constants.R_HEADS));
            } else if (StrUtil.startWith(last, Constants.R_TAGS) && !last.endsWith("^{}")) {
                tags.add(StrUtil.removePrefix(last, Constants.R_TAGS));
            }
        }
        branches.sort((o1, o2) -> VersionComparator.INSTANCE.compare(o2, o1));
        tags.sort((o1, o2) -> VersionComparator.INSTANCE.compare(o2, o1));
        return new Tuple(branches, tags);
    }

    @Override
    public String[] pullBranch() throws Exception {
        Assert.hasText(branchName, "未指定branch");
        return gitPull(branchName);
    }

    @Override
    public String[] pullTag() throws Exception {
        Assert.hasText(tagName, "未执行tag");
        return gitPull(tagName);
    }

    private String[] gitPull(String branchOrTag) throws IOException {
        if (this.needClone()) {
            this.gitClone(printWriter, branchOrTag);
        }

        {
            // git fetch
            int code = CommandUtil.exec(savePath, null, line -> {
                printWriter.println(line);
                printWriter.flush();
            }, List.of("git", "fetch", "--all"));
            if (code != 0 && strictlyEnforce != null && strictlyEnforce) {
                return new String[]{null, null, "git fetch失败状态码:" + code};
            }

            // git reset
            code = CommandUtil.exec(savePath, null, line -> {
                printWriter.println(line);
                printWriter.flush();
            }, List.of("git", "reset", "--hard", "origin/" + branchOrTag));
            if (code != 0 && strictlyEnforce != null && strictlyEnforce) {
                return new String[]{null, null, "git reset --hard失败状态码:" + code};
            }

            // git submodule
            code = CommandUtil.exec(savePath, null, line -> {
                printWriter.println(line);
                printWriter.flush();
            }, List.of("git", "submodule", "update", "--init", "--remote", "-f", "--recursive"));
            if (code != 0 && strictlyEnforce != null && strictlyEnforce) {
                return new String[]{null, null, "git submodule update 失败状态码:" + code};
            }
        }

        // git log
        String[] commitId = new String[1];
        StringBuilder commitInfo = new StringBuilder();

        AtomicBoolean nextMsg = new AtomicBoolean(false);
        CommandUtil.exec(savePath, null, line -> {
            printWriter.println(line);
            printWriter.flush();
            if (StrUtil.isEmpty(commitId[0]) && StrUtil.startWithIgnoreCase(line, "commit")) {
                List<String> list = StrUtil.splitTrim(line, StrUtil.SPACE);
                commitId[0] = CollUtil.get(list, 1);
            }
            if (StrUtil.startWithIgnoreCase(line, "Date:")) {
                nextMsg.set(true);
            } else if (nextMsg.get()) {
                commitInfo.append(line).append("\n");
            }
        }, List.of("git", "log", "-1", branchOrTag));
        return new String[]{commitId[0], commitInfo.toString()};
    }

    /**
     * 是否要重新拉取
     */
    private boolean needClone() throws MalformedURLException {
        File file = FileUtil.file(savePath, Constants.DOT_GIT);
        if (!FileUtil.exist(file)) {
            return true;
        }

        // 远程地址是否一样
        String checkRemote = CommandUtil.execCommand("git remote -v", savePath);
        if (!StrUtil.containsAny(checkRemote, remoteUrl, this.gitUrl())) {
            return true;
        }

        // 分支是否一样
        if (StrUtil.isNotEmpty(branchName)) {
            String checkBranch = CommandUtil.execCommand("git rev-parse --abbrev-ref HEAD", savePath);
            checkBranch = CollUtil.getLast(StrUtil.splitTrim(checkBranch, '\n'));
            return !StrUtil.equals(checkBranch, branchName);
        }
        return true;
    }

    private void gitClone(PrintWriter printWriter, String branchOrTag) throws IOException {
        // 清除目录
        if (!FileUtil.clean(savePath)) {
            FileUtil.del(savePath.toPath());
        }
        // clone深度
        String depthStr = Optional.ofNullable(depth).map(integer -> {
            if (integer > 0) {
                return integer;
            }
            return null;
        }).map(integer -> "--depth=" + integer).orElse(StrUtil.EMPTY);
        // clone超时
        Map<String, String> env = new HashMap<>(4);
        Optional.ofNullable(timeout).map(integer -> {
            if (integer > 0) {
                return integer;
            }
            return null;
        }).ifPresent(integer -> env.put("GIT_HTTP_TIMEOUT", String.valueOf(integer)));
        // 执行clone
        List<String> command = new ArrayList<>();
        command.add("git");
        command.add("clone");
        command.add("--recursive");
        if(StringUtils.isNotBlank(depthStr)){
            command.add(depthStr);
        }
        command.add("-b");
        command.add(branchOrTag);
        command.add(this.gitUrl());
        command.add(savePath.getAbsolutePath());

        FileUtil.mkdir(savePath);
        CommandUtil.exec(savePath, env, line -> {
            printWriter.println(line);
            printWriter.flush();
        }, command);
    }
}
