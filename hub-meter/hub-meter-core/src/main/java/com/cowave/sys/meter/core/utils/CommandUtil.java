package com.cowave.sys.meter.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.*;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author bwcx_jzy
 */
@Slf4j
public class CommandUtil {
    public static final String SUFFIX;
    public static final String SUFFIX_UNIX = "sh";
    public static final String SUFFIX_WINDOWS = "bat";
    private static final String EXECUTE_PREFIX;
    /**
     * 命令
     */
    private static final List<String> COMMAND = new ArrayList<>();
    /**
     * 是否缓存执行结果
     */
    private static final ThreadLocal<Boolean> ENABLE_COMMAND_CACHE = new ThreadLocal<>();
    /**
     * 缓存执行结果
     */
    private static final ThreadLocal<Map<String, String>> COMMAND_CACHE = new ThreadLocal<>();

    static {
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isLinux() || osInfo.isMac() || osInfo.isMacOsX() || osInfo.isIrix() || osInfo.isHpUx()) {
            //执行linux系统命令
            COMMAND.add("/bin/bash");
            COMMAND.add("-c");
        } else if (osInfo.isWindows()) {
            COMMAND.add("cmd");
            COMMAND.add("/c");
        } else {
            log.error("不支持的系统类型：{}", osInfo.getName());
        }

        if (osInfo.isWindows()) {
            SUFFIX = SUFFIX_WINDOWS;
            EXECUTE_PREFIX = StrUtil.EMPTY;
        } else {
            SUFFIX = SUFFIX_UNIX;
            EXECUTE_PREFIX = "bash";
        }
    }

    /**
     * 获取执行命令
     */
    public static List<String> getCommand() {
        return ObjectUtil.clone(COMMAND);
    }

    /**
     * 执行命令
     */
    public static String execCommand(String command) {
        Boolean cache = ENABLE_COMMAND_CACHE.get();
        if (cache != null && cache) {
            Map<String, String> cacheMap = COMMAND_CACHE.get();
            return cacheMap.computeIfAbsent(command, key -> execCommand(key, null));
        }
        return execCommand(command, null);
    }

    /**
     * 在指定目录下执行命令
     */
    public static String execCommand(String command, File dir) {
        return execCommand(command, dir, null);
    }

    /**
     * 在指定目录下执行命令
     */
    public static String execCommand(String command, File file, Map<String, String> map) {
        String newCommand = StrUtil.replace(command, StrUtil.CRLF, StrUtil.SPACE);
        newCommand = StrUtil.replace(newCommand, StrUtil.LF, StrUtil.SPACE);
        String result = "error";
        try {
            List<String> commands = getCommand();
            commands.add(newCommand);
            result = exec(commands, file, map);
        } catch (Exception e) {
            if (ExceptionUtil.isCausedBy(e, InterruptedException.class)) {
                log.warn("执行被中断：{}", command);
                result += "执行被中断";
            } else {
                log.error("执行命令异常", e);
                result += e.getMessage();
            }
        }
        return result;
    }

    /**
     * 执行命令
     */
    private static String exec(List<String> cmd, File file, Map<String, String> env) throws IOException {
        boolean isLog;
        Charset charset;
        try {
            charset = ExtConfigFile.getConsoleLogCharset();
            isLog = true;
        } catch (Exception e) {
            isLog = false;
            charset = CharsetUtil.systemCharset();
        }

        List<String> resultList = new ArrayList<>();
        int code = exec(file, env, charset, resultList::add, cmd);
        String result = String.join(StrUtil.LF, resultList);
        if (isLog) {
            log.debug("exit {}, {}", code, result);
        }
        return result;
    }

    /**
     * 在指定目录下执行命令
     */
    public static int exec(File file, Map<String, String> env, LineHandler lineHandler, List<String> cmd) throws IOException {
        Charset charset;
        try {
            charset = ExtConfigFile.getConsoleLogCharset();
        } catch (Exception e) {
            charset = CharsetUtil.systemCharset();
        }
        return exec(file, env, charset, lineHandler, cmd);
    }

    /**
     * 执行命令
     */
    public static int exec(File file, Map<String, String> env, Charset charset, LineHandler lineHandler, List<String> cmd) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);

        File dir = file == null ? new File(System.getProperty("user.dir")) : file;
        lineHandler.handle(">> " + dir.getAbsolutePath() + " " + String.join(" ", processBuilder.command()));

        Map<String, String> environment = processBuilder.directory(file).environment();
        // 环境变量
        Optional.ofNullable(env).ifPresent(environment::putAll);
        Process process = processBuilder.redirectErrorStream(true).start();
        Charset charset2 = ObjectUtil.defaultIfNull(charset, CharsetUtil.defaultCharset());
        InputStream in = null;
        try {
            in = process.getInputStream();
            IoUtil.readLines(in, charset2, lineHandler);
            return process.waitFor();
        } catch (InterruptedException e) {
            throw Lombok.sneakyThrow(e);
        } finally {
            IoUtil.close(in);
            RuntimeUtil.destroy(process);
        }
    }

    public static List<String> build(File commandDir, String args) {
        List<String> list = StrUtil.splitTrim(args, StrUtil.SPACE);
        String path = FileUtil.getAbsolutePath(commandDir);
        list.add(0, path);
        CommandUtil.paddingPrefix(list);
        return list;
    }

    public static void paddingPrefix(List<String> list) {
        if (EXECUTE_PREFIX.isEmpty()) {
            return;
        }
        list.add(0, CommandUtil.EXECUTE_PREFIX);
    }



















    public static String generateCommand(File file, String args) {
        String path = FileUtil.getAbsolutePath(file);
        return generateCommand(path, args);
    }

    public static String generateCommand(String file, String args) {
        return StrUtil.format("{} {} {}", CommandUtil.EXECUTE_PREFIX, file, args);
        //String command = CommandUtil.EXECUTE_PREFIX + StrUtil.SPACE + FileUtil.getAbsolutePath(scriptFile) + " restart upgrade";
    }

    /**
     * 开启缓存执行结果
     */
    public static void openCache() {
        ENABLE_COMMAND_CACHE.set(true);
        COMMAND_CACHE.set(new ConcurrentHashMap<>(16));
    }

    /**
     * 关闭缓存执行结果
     */
    public static void closeCache() {
        ENABLE_COMMAND_CACHE.remove();
        COMMAND_CACHE.remove();
    }

    /**
     * 执行命令
     *
     * @param cmd 命令行
     * @return 结果
     * @throws IOException IO
     */
    private static String exec(List<String> cmd, File file) throws IOException {
        return exec(cmd, file, null);
    }

    /**
     * 异步执行命令
     *
     * @param file    文件夹
     * @param command 命令
     * @throws IOException 异常
     */
    public static void asyncExeLocalCommand(String command, File file) throws Exception {
        asyncExeLocalCommand(command, file, null);
    }

    /**
     * 异步执行命令
     *
     * @param file    文件夹
     * @param env     环境变量
     * @param command 命令
     * @throws IOException 异常
     */
    public static void asyncExeLocalCommand(String command, File file, Map<String, String> env) throws Exception {
        asyncExeLocalCommand(command, file, env, false);
    }

    /**
     * 异步执行命令
     *
     * @param file        文件夹
     * @param env         环境变量
     * @param hopeUseSudo 是否期望填充 sudo
     * @param command     命令
     * @throws IOException 异常
     */
    public static void asyncExeLocalCommand(String command, File file, Map<String, String> env, boolean hopeUseSudo) throws Exception {
        String newCommand = StrUtil.replace(command, StrUtil.CRLF, StrUtil.SPACE);
        newCommand = StrUtil.replace(newCommand, StrUtil.LF, StrUtil.SPACE);
        boolean jpomCommandUseSudo = SystemUtil.getBoolean("JPOM_COMMAND_USE_SUDO", false);
        if (hopeUseSudo && jpomCommandUseSudo) {
            // 期望使用 sudo 并且配置了开启 sudo
            newCommand = StrUtil.addPrefixIfNot(newCommand, "sudo ");
        }
        //
        log.debug(newCommand);
        List<String> commands = getCommand();
        commands.add(newCommand);
        ProcessBuilder pb = new ProcessBuilder(commands);
        if (file != null) {
            pb.directory(file);
        }
        Map<String, String> environment = pb.environment();
        if (env != null) {
            environment.putAll(env);
        }
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.start();
    }


    /**
     * 判断是否包含删除命令
     *
     * @param script 命令行
     * @return true 包含
     */
    public static boolean checkContainsDel(String script) {
        // 判断删除
        String[] commands = StrUtil.splitToArray(script, StrUtil.LF);
        for (String commandItem : commands) {
            if (checkContainsDelItem(commandItem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行系统命令 快速删除.
     * 执行删除后再检查文件是否存在
     *
     * @param file 文件或者文件夹
     * @return true 文件还存在
     */
    public static boolean systemFastDel(File file) {
        String path = FileUtil.getAbsolutePath(file);
        String command;
        if (SystemUtil.getOsInfo().isWindows()) {
            // Windows
            command = StrUtil.format("rd /s/q \"{}\"", path);
        } else {
            // Linux MacOS
            command = StrUtil.format("rm -rf '{}'", path);
        }
        CommandUtil.execCommand(command);
        // 再次尝试
        boolean del = FileUtil.del(file);
        if (!del) {
            FileUtil.del(file.toPath());
        }
        return FileUtil.exist(file);
    }

    private static boolean checkContainsDelItem(String script) {
        String[] split = StrUtil.splitToArray(script, StrUtil.SPACE);
        if (SystemUtil.getOsInfo().isWindows()) {
            for (String s : split) {
                if (StrUtil.startWithAny(s, "rd", "del")) {
                    return true;
                }
                if (StrUtil.containsAnyIgnoreCase(s, " rd", " del")) {
                    return true;
                }
            }
        } else {
            for (String s : split) {
                if (StrUtil.startWithAny(s, "rm", "\\rm")) {
                    return true;
                }
                if (StrUtil.containsAnyIgnoreCase(s, " rm", " \\rm", "&rm", "&\\rm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 执行脚本
     *
     * @param scriptFile 脚本文件
     * @param baseDir    基础路径
     * @param env        环境变量
     * @param args       参数
     * @param consumer   回调
     * @return 退出码
     * @throws IOException          io
     * @throws InterruptedException 异常
     */
    public static int execWaitFor(File scriptFile, File baseDir, Map<String, String> env, String args, BiConsumer<String, Process> consumer) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        //
        List<String> command = build(scriptFile, args);
        log.debug(CollUtil.join(command, StrUtil.SPACE));
        processBuilder.redirectErrorStream(true);
        processBuilder.command(command);
        Optional.ofNullable(baseDir).ifPresent(processBuilder::directory);
        Map<String, String> environment = processBuilder.environment();
        environment.replaceAll((k, v) -> Optional.ofNullable(v).orElse(StrUtil.EMPTY));
        // 新增逻辑,将env和environment里value==null替换成空字符,防止putAll出现空指针报错
        if (env != null) {
            // 环境变量
            env.replaceAll((k, v) -> Optional.ofNullable(v).orElse(StrUtil.EMPTY));
            environment.putAll(env);
        }
        //
        Process process = processBuilder.start();
        try (InputStream inputStream = process.getInputStream()) {
            IoUtil.readLines(inputStream, ExtConfigFile.getConsoleLogCharset(), (LineHandler) line -> consumer.accept(line, process));
        }
        return process.waitFor();
    }

    /**
     * 关闭 Process实例
     *
     * @param process Process
     */
    public static void kill(Process process) {
        if (process == null) {
            return;
        }
        while (true) {
            Object handle = tryGetProcessId(process);
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
                try {
                    process.waitFor(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
                log.info("等待关闭[Process]进程：{}", handle);
            } else {
                break;
            }
        }
    }

    public static Object tryGetProcessId(Process process) {
        Object handle = ReflectUtil.getFieldValue(process, "handle");
        Object pid = ReflectUtil.getFieldValue(process, "pid");
        return Optional.ofNullable(handle).orElse(pid);
    }
}
