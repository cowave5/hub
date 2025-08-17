package com.cowave.sys.meter.core.build;

import cn.hutool.core.util.CharsetUtil;
import com.cowave.sys.meter.core.build.process.*;
import com.cowave.sys.meter.core.utils.LogRecorder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author bwcx_jzy
 */
@Slf4j
public class BuildTask implements Runnable {

    private final LogRecorder logRecorder = new LogRecorder(new PrintWriter(System.out), CharsetUtil.CHARSET_UTF_8);

    private final File workHome;

    public BuildTask(File workHome) {
        this.workHome = workHome;
    }

    @Override
    public void run() {
        try {
            executeScript();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行构建脚本
     */
    public void executeScript() throws IOException {
        Map<String, ProcessItem> processMap = createProcess();

        String processName = "";
        for (Map.Entry<String, ProcessItem> entry : processMap.entrySet()) {
            processName = entry.getKey();
            ProcessItem processItem = entry.getValue();

            long processStartTime = System.currentTimeMillis();
            logRecorder.system("================================================ {}", processItem.name());

            Integer exitCode = processItem.process();
            if(exitCode != 0){
                logRecorder.systemError("failed");
            }
        }
    }

    private Map<String, ProcessItem> createProcess() {
        Map<String, ProcessItem> suppliers = new LinkedHashMap<>(10);
        suppliers.put("ready", new ReadyProcess());
        suppliers.put("pull", new PullProcess());
        suppliers.put("build", new BuildProcess(workHome, logRecorder));
        suppliers.put("package", new PackageProcess());
        suppliers.put("release", new ReleaseProcess());
        suppliers.put("finish", new FinishProcess());
        return suppliers;
    }
}
