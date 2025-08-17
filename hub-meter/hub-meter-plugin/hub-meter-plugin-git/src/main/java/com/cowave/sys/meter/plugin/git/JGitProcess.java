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

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import com.cowave.sys.meter.core.plugin.IWorkspaceEnvPlugin;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.eclipse.jgit.errors.NotSupportedException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Hong
 */
@Slf4j
public class JGitProcess extends AbstractGitProcess {

    protected JGitProcess(IWorkspaceEnvPlugin workspaceEnvPlugin, Map<String, Object> parameter) {
        super(workspaceEnvPlugin, parameter);
    }

    @Override
    public Tuple listBranchAndTag() throws Exception {
        try {
            LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository().setRemote(remoteUrl);
            // 更新凭证
            setCredentials(lsRemoteCommand);
            Collection<Ref> call = lsRemoteCommand.setHeads(true).setTags(true).call();
            if (CollUtil.isEmpty(call)) {
                return null;
            }
            Map<String, List<Ref>> refMap = CollStreamUtil.groupByKey(call, ref -> {
                String name = ref.getName();
                if (name.startsWith(Constants.R_TAGS)) {
                    return Constants.R_TAGS;
                } else if (name.startsWith(Constants.R_HEADS)) {
                    return Constants.R_HEADS;
                }
                return null;
            });

            // branch list
            List<Ref> branchListRef = refMap.get(Constants.R_HEADS);
            if (branchListRef == null) {
                return null;
            }

            List<String> branchList = branchListRef.stream().map(ref -> {
                        String name = ref.getName();
                        if (name.startsWith(Constants.R_HEADS)) {
                            return name.substring((Constants.R_HEADS).length());
                        }
                        return null;
                    }
            ).filter(Objects::nonNull).sorted((o1, o2) -> VersionComparator.INSTANCE.compare(o2, o1)).toList();

            // list tag
            List<Ref> tagListRef = refMap.get(Constants.R_TAGS);
            List<String> tagList = tagListRef == null ? new ArrayList<>() : tagListRef.stream().map(ref -> {
                        String name = ref.getName();
                        if (name.startsWith(Constants.R_TAGS)) {
                            return name.substring((Constants.R_TAGS).length());
                        }
                        return null;
                    }
            ).filter(Objects::nonNull).sorted((o1, o2) -> VersionComparator.INSTANCE.compare(o2, o1)).toList();
            return new Tuple(branchList, tagList);
        } catch (Exception t) {
            checkTransportException(t, null, null);
            return null;
        }
    }

    @Override
    public String[] pullBranch() throws Exception {
        String path = FileUtil.getAbsolutePath(savePath);
        synchronized (StrUtil.concat(false, remoteUrl, path).intern()) {
            try (Git git = initGit(branchName, null, savePath, printWriter)) {
                pull(git, branchName, printWriter);
                return getLastCommitMsg(savePath, false, branchName);
            } catch (Exception t) {
                checkTransportException(t, savePath, printWriter);
            }
        }
        return new String[]{StrUtil.EMPTY, StrUtil.EMPTY};
    }

    @Override
    public String[] pullTag() throws Exception {
        String path = FileUtil.getAbsolutePath(savePath);
        synchronized (StrUtil.concat(false, remoteUrl, path).intern()) {
            try (Git git = initGit(null, tagName, savePath, printWriter)) {
                // 获取最后提交信息
                return getLastCommitMsg(savePath, true, tagName);
            } catch (Exception t) {
                checkTransportException(t, savePath, printWriter);
            }
        }
        return new String[]{StrUtil.EMPTY, StrUtil.EMPTY};
    }

    private void setCredentials(TransportCommand<?, ?> transportCommand) {
        // timeout
        Optional.ofNullable(timeout).map(integer -> integer <= 0 ? null : integer).ifPresent(transportCommand::setTimeout);
        // http
        if (protocol == 0) {
            CredentialsProvider credentialsProvider = new SslVerifyUsernamePasswordCredentialsProvider(username, password);
            transportCommand.setCredentialsProvider(credentialsProvider);
        } else if (protocol == 1) {
            // ssh
            transportCommand.setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(new JschConfigSessionFactory() {
                    @Override
                    protected void configure(OpenSshConfig.Host hc, Session session) {
                        session.setConfig("StrictHostKeyChecking", "no");
                        // ssh 需要单独设置超时
                        Optional.ofNullable(timeout).map(
                                integer -> integer <= 0 ? null : integer).ifPresent(integer -> {
                            try {
                                session.setTimeout(integer * 1000);
                            } catch (JSchException e) {
                                throw Lombok.sneakyThrow(e);
                            }
                        });
                    }

                    @Override
                    protected JSch createDefaultJSch(FS fs) throws JSchException {
                        JSch jSch = super.createDefaultJSch(fs);
                        if (rsaFile == null) {
                            return jSch;
                        }
                        // 添加私钥文件
                        if (StrUtil.isEmpty(password)) {
                            jSch.addIdentity(rsaFile.getPath());
                        } else {
                            jSch.addIdentity(rsaFile.getPath(), password);
                        }
                        return jSch;
                    }
                });
            });
        } else {
            throw new IllegalStateException("不支持的协议类型");
        }
    }

    public void checkTransportException(Exception ex, File gitFile, PrintWriter printWriter) throws Exception {
        println(printWriter, "");
        Throwable causedBy = ExceptionUtil.getCausedBy(ex, NotSupportedException.class);
        if (causedBy != null) {
            println(printWriter, "当前地址可能不是 git 仓库地址：" + causedBy.getMessage());
            throw new IllegalStateException("当前地址可能不是 git 仓库地址：" + causedBy.getMessage(), ex);
        }
        causedBy = ExceptionUtil.getCausedBy(ex, NoRemoteRepositoryException.class);
        if (causedBy != null) {
            println(printWriter, "当前地址远程不存在仓库：" + causedBy.getMessage());
            throw new IllegalStateException("当前地址远程不存在仓库：" + causedBy.getMessage(), ex);
        }
        causedBy = ExceptionUtil.getCausedBy(ex, RepositoryNotFoundException.class);
        if (causedBy != null) {
            println(printWriter, "当前地址不存在仓库：" + causedBy.getMessage());
            throw new IllegalStateException("当前地址不存在仓库：" + causedBy.getMessage(), ex);
        }
        if (ex instanceof TransportException) {
            String msg = ex.getMessage();
            if (StrUtil.containsAny(msg, JGitText.get().notAuthorized, JGitText.get().authenticationNotSupported)) {
                throw new IllegalArgumentException("账号密码不正确或者不支持的身份验证," + msg, ex);
            }
            throw ex;
        } else if (ex instanceof NoHeadException) {
            println(printWriter, "拉取代码异常,已经主动清理本地仓库缓存内容,请手动重试" + ex.getMessage());
            if (gitFile == null) {
                throw ex;
            } else {
                FileUtil.del(gitFile);
            }
            throw ex;
        } else if (ex instanceof CheckoutConflictException) {
            println(printWriter, "拉取代码发生冲突,可以尝试清除构建或者解决仓库里面的冲突后重新操作：" + ex.getMessage());
            throw ex;
        } else {
            println(printWriter, "拉取代码发生未知异常建议清除构建重新操作：" + ex.getMessage());
            throw ex;
        }
    }

    private void println(PrintWriter printWriter, CharSequence template, Object... params) {
        if (printWriter == null) {
            return;
        }
        printWriter.println(StrUtil.format(template, params));
        IoUtil.flush(printWriter);
    }

    private Git initGit(String branchName, String tagName, File file, PrintWriter printWriter) {
        return Optional.of(file).flatMap(file12 -> {
                    if (FileUtil.file(file12, Constants.DOT_GIT).exists()) {
                        return Optional.of(true);
                    }
                    return Optional.empty();
                }).flatMap(status -> {
                    try {
                        if (checkRemoteUrl(remoteUrl, file)) {
                            return Optional.of(true);
                        }
                    } catch (IOException | GitAPIException e) {
                        throw Lombok.sneakyThrow(e);
                    }
                    return Optional.empty();
                }).flatMap(aBoolean -> {
                    if (StrUtil.isEmpty(tagName)) {
                        // 分支模式，继续验证
                        return Optional.of(true);
                    }
                    // 标签模式直接中断
                    return Optional.empty();
                })
                .flatMap(status -> {
                    // 本地分支
                    try {
                        // 远程地址
                        if (checkBranchName(branchName, file)) {
                            return Optional.of(true);
                        }
                    } catch (IOException | GitAPIException e) {
                        throw Lombok.sneakyThrow(e);
                    }
                    return Optional.empty();
                }).map(aBoolean -> {
                    try {
                        return aBoolean ? Git.open(file) : reClone(branchName, tagName, file, printWriter);
                    } catch (IOException | GitAPIException e) {
                        throw Lombok.sneakyThrow(e);
                    }
                }).orElseGet(() -> {
                    try {
                        return reClone(branchName, tagName, file, printWriter);
                    } catch (GitAPIException | IOException e) {
                        throw Lombok.sneakyThrow(e);
                    }
                });
    }

    public boolean checkRemoteUrl(String url, File file) throws IOException, GitAPIException {
        try (Git git = Git.open(file)) {
            boolean urlTrue = false;
            RemoteListCommand remoteListCommand = git.remoteList();
            List<RemoteConfig> list = remoteListCommand.call();
            end:
            for (RemoteConfig remoteConfig : list) {
                for (URIish urIish : remoteConfig.getURIs()) {
                    if (urIish.toString().equals(url)) {
                        urlTrue = true;
                        break end;
                    }
                }
            }
            return urlTrue;
        } catch (NoRemoteRepositoryException ex) {
            log.warn("JGit: No remote repository found for url: {}", url);
            return false;
        }
    }

    private boolean checkBranchName(String branchName, File file) throws IOException, GitAPIException {
        try (Git pullGit = Git.open(file)) {
            // 判断本地是否存在对应分支
            List<Ref> list = pullGit.branchList().call();
            for (Ref ref : list) {
                String name = ref.getName();
                if (StrUtil.equals(name, Constants.R_HEADS + branchName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Git reClone(String branchName, String tagName, File file, PrintWriter printWriter) throws GitAPIException, IOException {
        if (!FileUtil.clean(file)) {
            FileUtil.del(file.toPath());
        }

        CloneCommand cloneCommand = Git.cloneRepository();
        if (printWriter != null) {
            cloneCommand.setProgressMonitor(new SmallTextProgressMonitor(printWriter, progressRatio));
        }
        if (branchName != null) {
            cloneCommand.setBranch(Constants.R_HEADS + branchName);
            cloneCommand.setBranchesToClone(Collections.singletonList(Constants.R_HEADS + branchName));
        }
        if (tagName != null) {
            cloneCommand.setBranch(Constants.R_TAGS + tagName);
        }
        CloneCommand command = cloneCommand.setURI(remoteUrl).setDirectory(file).setCloneSubmodules(true);
        setCredentials(command);
        return command.call();
    }

    private PullResult pull(Git git, String branchName, PrintWriter printWriter) throws Exception {
        SmallTextProgressMonitor progressMonitor = new SmallTextProgressMonitor(printWriter, progressRatio);
        // reset
        git.checkout().setName(branchName).setForced(true).setProgressMonitor(progressMonitor).call();
        // pull
        PullCommand pull = git.pull();
        setCredentials(pull);
        PullResult call = pull
                .setRemoteBranchName(branchName)
                .setProgressMonitor(progressMonitor)
                .setRecurseSubmodules(SubmoduleConfig.FetchRecurseSubmodulesMode.YES).call();
        // 输出拉取结果
        if (call != null) {
            // String fetchedFrom = call.getFetchedFrom();
            // FetchResult fetchResult = call.getFetchResult();
            // RebaseResult rebaseResult = call.getRebaseResult();
            MergeResult mergeResult = call.getMergeResult();
            if (mergeResult != null) {
                println(printWriter, mergeResult.toString());
            }
        }
        // 子模块
        SubmoduleUpdateCommand subUpdate = git.submoduleUpdate();
        setCredentials(subUpdate);
        Collection<String> rst = subUpdate
                .setProgressMonitor(progressMonitor)
                .setStrategy(MergeStrategy.THEIRS)
                .setFetch(true)
                .call();
        for (String path : rst) {
            println(printWriter, path);
        }
        return call;
    }

    public String[] getLastCommitMsg(File file, boolean tag, String refName) throws IOException, GitAPIException {
        try (Git git = Git.open(file)) {
            ObjectId anyObjectId = tag ? getTagAnyObjectId(git, refName) : getBranchAnyObjectId(git, refName);
            Objects.requireNonNull(anyObjectId, StrUtil.format("没有 {} 分支/标签", refName));
            String lastCommitMsg = getLastCommitMsg(file, refName, anyObjectId);
            return new String[]{anyObjectId.getName(), lastCommitMsg};
        }
    }

    private String getLastCommitMsg(File file, String desc, ObjectId objectId) throws IOException {
        try (Git git = Git.open(file)) {
            RevWalk walk = new RevWalk(git.getRepository());
            RevCommit revCommit = walk.parseCommit(objectId);
            String time = new DateTime(revCommit.getCommitTime() * 1000L).toString();
            PersonIdent personIdent = revCommit.getAuthorIdent();
            return StrUtil.format("{} {} {}[{}] {} {}",
                    desc,
                    revCommit.getShortMessage(),
                    personIdent.getName(),
                    personIdent.getEmailAddress(),
                    time,
                    revCommit.getParentCount());
        }
    }

    private ObjectId getTagAnyObjectId(Git git, String tagName) throws GitAPIException {
        List<Ref> list = git.tagList().call();
        for (Ref ref : list) {
            String name = ref.getName();
            if (name.startsWith(Constants.R_TAGS + tagName)) {
                return ref.getObjectId();
            }
        }
        return null;
    }

    private static ObjectId getBranchAnyObjectId(Git git, String branchName) throws GitAPIException {
        List<Ref> list = git.branchList().call();
        for (Ref ref : list) {
            String name = ref.getName();
            if (name.startsWith(Constants.R_HEADS + branchName)) {
                return ref.getObjectId();
            }
        }
        return null;
    }
}
