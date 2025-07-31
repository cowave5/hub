package com.cowave.sys.meter.core.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.cowave.commons.tools.SpringContext;
import com.cowave.sys.meter.core.JpomApplication;
import com.cowave.sys.meter.core.common.JpomManifest;
import lombok.Lombok;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Function;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;

/**
 * @author bwcx_jzy
 */
public class ExtConfigFile {

    /**
     * 项目运行存储路径
     */
    private static String path;

    /**
     * 控制台日志编码
     */
    private static Charset consoleLogCharset;

    public static void setConsoleLogCharset(Charset consoleLogCharset) {
        ExtConfigFile.consoleLogCharset = consoleLogCharset;
    }

    public static Charset getConsoleLogCharset() {
        return ObjectUtil.defaultIfNull(consoleLogCharset, CharsetUtil.systemCharset());
    }

    public static void setPath(String path) {
        ExtConfigFile.path = path;
    }

    /**
     * 获取外部文件流
     */
    public static InputStream tryGetConfigResourceInputStream(String name) {
        FileUtils.checkSlip(name);
        File configResourceDir = getConfigResourceDir();
        return Opt.ofBlankAble(configResourceDir).map((Function<File, InputStream>) configDir -> {
                File file = FileUtil.file(configDir, name);
                if (FileUtil.isFile(file)) {
                    return FileUtil.getInputStream(file);
                }
                return null;
            }).orElseGet(() -> tryGetDefaultConfigResourceInputStream(name));
    }

    /**
     * 获取外部文件流
     */
    public static InputStream tryGetDefaultConfigResourceInputStream(String name) {
        String normalize = FileUtil.normalize("/config_default/" + name);
        ClassPathResource classPathResource = new ClassPathResource(normalize);
        if (!classPathResource.exists()) {
            return null;
        }
        try {
            return classPathResource.getInputStream();
        } catch (IOException e) {
            throw Lombok.sneakyThrow(e);
        }
    }

    /**
     * 获取配置资源目录
     */
    public static File getConfigResourceDir() {
        String property = SpringContext.getProperty(CONFIG_LOCATION_PROPERTY, null);
        return Opt.ofBlankAble(property).map(s -> {
            File file = FileUtil.file(s);
            return FileUtil.getParent(file, 1);
        }).orElse(null);
    }

    public static String getPath() {
        if (StrUtil.isEmpty(path)) {
            if (JpomManifest.getInstance().isDebug()) {
                File newFile;
                String jpomDevPath = SystemUtil.get("JPOM_DEV_PATH");
                if (StrUtil.isNotEmpty(jpomDevPath)) {
                    newFile = FileUtil.file(jpomDevPath, JpomApplication.getAppType().name().toLowerCase());
                } else {
                    // 调试模式 为根路径的 jpom文件
                    newFile = FileUtil.file(FileUtil.getUserHomeDir(), "jpom", JpomApplication.getAppType().name().toLowerCase());
                }
                path = FileUtil.getAbsolutePath(newFile);
            } else {
                // 获取当前项目运行路径的父级
                File file = JpomManifest.getRunPath();
                if (!file.exists() && !file.isFile()) {
                    throw new RuntimeException("请配置运行路径属性【jpom.path】");
                }
                File parentFile = file.getParentFile().getParentFile();
                path = FileUtil.getAbsolutePath(parentFile);
            }
        }
        return FileUtil.normalize(path);
    }
}
