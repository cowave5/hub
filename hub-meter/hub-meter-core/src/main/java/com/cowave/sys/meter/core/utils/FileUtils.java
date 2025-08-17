package com.cowave.sys.meter.core.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Lombok;

import java.io.File;
import java.nio.charset.Charset;
import java.util.function.Function;

/**
 * @author bwcx_jzy
 */
public class FileUtils {

    /**
     * 目录是否越级
     */
    public static void checkSlip(String dir) {
        checkSlip(dir, e -> new IllegalArgumentException("目录不能越级: " + e.getMessage()));
    }

    /**
     * 目录是否越级
     */
    public static void checkSlip(String dir, Function<Exception, Exception> function) {
        try {
            File tmpDir = FileUtil.getTmpDir();
            FileUtil.checkSlip(tmpDir, FileUtil.file(tmpDir, dir));
        } catch (IllegalArgumentException e) {
            throw Lombok.sneakyThrow(function.apply(e));
        }
    }

    /**
     * 使用当前系统的换行符写文件
     */
    public static void writeScript(String context, File scriptFile, Charset charset) {
        String replace = StrUtil.replace(context, StrUtil.LF, FileUtil.getLineSeparator());
        FileUtil.writeString(replace, scriptFile, charset);
    }
}
