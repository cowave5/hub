package com.cowave.sys.meter.core.build.process;

import cn.hutool.core.io.IoUtil;
import com.cowave.sys.meter.core.JpomApplication;
import com.cowave.sys.meter.core.utils.CommandExec;
import com.cowave.sys.meter.core.utils.LogRecorder;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bwcx_jzy
 */
@RequiredArgsConstructor
public class BuildProcess implements ProcessItem {

    /**
     * 执行id
     */
    private String processId = "xxxx1";

    /**
     * 执行参数
     */
    private String args = "";

    /**
     * 执行脚本
     */
    private String processScript = "echo 123";

    /**
     * 环境变量
     */
    private Map<String, String> env = new HashMap<>();

    /**
     * 执行目录
     */
    private final File workHome;

    /**
     * 执行输出
     */
    private final LogRecorder logRecorder;

    @Override
    public String name() {
        return "Build";
    }

    @Override
    public Integer process() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("script/template.sh")) {
            String script = IoUtil.readUtf8(inputStream) + processScript;
            return JpomApplication.getInstance().execScript(script, file -> {
                try {
                    return CommandExec.execScript(processId, file, workHome, env, "", logRecorder);
                } catch (Exception e) {
                    throw Lombok.sneakyThrow(e);
                }
            });
        } catch (Exception e) {
            logRecorder.info(e.getMessage());
            return -1;
        }
    }
}
