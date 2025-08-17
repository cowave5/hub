package com.cowave.sys.meter.core.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.apache.commons.exec.environment.EnvironmentUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bwcx_jzy
 */
@Slf4j
public class CommandExec {

    private static final Map<String, Process> processMap = new ConcurrentHashMap<>();

    private static final ShutdownHookProcessDestroyer shutdownHookProcessDestroyer = new ShutdownHookProcessDestroyer();

    public static void addProcess(Process process) {
        shutdownHookProcessDestroyer.add(process);
    }

    public static void kill(String processId) {
        Process process = processMap.remove(processId);
        if (process == null) {
            return;
        }
        CommandUtil.kill(process);
    }

    /**
     * 执行脚本
     *
     * @param scriptFile  脚本文件
     * @param commandHome 执行目录
     * @param env         环境变量
     * @param args        执行参数
     * @param logRecorder 日志记录
     * @return 退出码
     */
    public static int execScript(String processId, File scriptFile, File commandHome, Map<String, String> env, String args, LogRecorder logRecorder) throws IOException {
        List<String> list = CommandUtil.build(scriptFile, args);
        String command = String.join(StrUtil.SPACE, list);
        CommandLine commandLine = CommandLine.parse(command);
        log.debug(command);

        // 环境变量
        Map<String, String> procEnvironment = EnvironmentUtils.getProcEnvironment();
        procEnvironment.putAll(env);

        // 输出流
        Charset charset;
        try {
            charset = ExtConfigFile.getConsoleLogCharset();
        } catch (Exception e) {
            charset = CharsetUtil.systemCharset();
        }
        final LogOutputStream logOutputStream = new LogOutputStream(1, charset) {
            @Override
            protected void processLine(String line, int logLevel) {
                logRecorder.info(line);
            }
        };
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(logOutputStream, logOutputStream);

        // 执行器
        DefaultExecutor executor = DefaultExecutor.builder()
                .setExecuteStreamHandler(pumpStreamHandler)
                .setWorkingDirectory(commandHome)
                .get();
        executor.setProcessDestroyer(new ProcessDestroyer() {
            private int size = 0;
            @Override
            public boolean add(Process process) {
                processMap.put(processId, process);
                size++;
                return shutdownHookProcessDestroyer.add(process);
            }
            @Override
            public boolean remove(Process process) {
                processMap.remove(processId);
                size--;
                return shutdownHookProcessDestroyer.remove(process);
            }
            @Override
            public int size() {
                return size;
            }
        });
        pumpStreamHandler.stop();
        // 执行，打印退出码
        try {
            return executor.execute(commandLine, procEnvironment);
        } catch (ExecuteException executeException) {
            logRecorder.systemWarning("执行异常：{}", executeException.getMessage());
            return executeException.getExitValue();
        }
    }
}
