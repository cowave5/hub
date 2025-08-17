/*
 * Copyright (c) 2019 Of Him Code Technology Studio
 * Jpom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * 			http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package com.cowave.sys.meter.core.plugin;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.JarClassLoader;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.cowave.sys.meter.core.common.JpomManifest;
import com.cowave.sys.meter.core.utils.ExtConfigFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.io.File;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToIntFunction;

/**
 * @author bwcx_jzy
 */
@Slf4j
public class PluginFactory implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationListener<ApplicationEvent> {

    private static final Map<String, List<PluginItemWrap>> PLUGIN_HOLD = new ConcurrentHashMap<>();

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Set<Class<?>> classes = ClassUtil.scanPackage("com.cowave.sys.meter.plugin", IPlugin.class::isAssignableFrom);
        List<PluginItemWrap> pluginItemWraps = classes.stream()
                .filter(clazz -> ClassUtil.isNormalClass(clazz) && clazz.isAnnotationPresent(Plugin.class))
                .map(clazz -> new PluginItemWrap((Class<? extends IPlugin>) clazz))
                .filter(pluginItemWrap -> {
                    if (StrUtil.isEmpty(pluginItemWrap.getName())) {
                        log.warn("invalid plugin name, {}", pluginItemWrap.getClassName());
                        return false;
                    }
                    log.info("load plugin ============> {}[{}]", pluginItemWrap.getName(), pluginItemWrap.getClassName());
                    return true;
                }).toList();
        // 加载
        Map<String, List<PluginItemWrap>> pluginMap = CollStreamUtil.groupByKey(pluginItemWraps, PluginItemWrap::getName);
        pluginMap.forEach((key, value) -> {
            value.sort((o1, o2) -> Comparator.comparingInt(
                    (ToIntFunction<PluginItemWrap>) value1 -> {
                        Order order = value1.getClassName().getAnnotation(Order.class);
                        if (order == null) {
                            return 0;
                        }
                        return order.value();
                    }).compare(o1, o2));
            PLUGIN_HOLD.put(key, value);
        });
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            Collection<List<PluginItemWrap>> values = PLUGIN_HOLD.values();
            for (List<PluginItemWrap> value : values) {
                for (PluginItemWrap pluginItemWrap : value) {
                    IoUtil.close(pluginItemWrap.getPlugin());
                }
            }
        } else if (event instanceof ApplicationReadyEvent) {
            System.setProperty(IPlugin.DATE_PATH_KEY, ExtConfigFile.getPath());
            System.setProperty(IPlugin.JPOM_VERSION_KEY, JpomManifest.getInstance().getVersion());
        }
    }

    /**
     * 获取插件端
     *
     * @param name 插件名
     * @return 插件对象
     */
    public static IPlugin getPlugin(String name) {
        List<PluginItemWrap> pluginItemWraps = PLUGIN_HOLD.get(name);
        PluginItemWrap first = CollUtil.getFirst(pluginItemWraps);
        Assert.notNull(first, "对应找到对应到插件：" + name);
        return first.getPlugin();
    }

    /**
     * 判断是否包含某个插件
     *
     * @param name 插件名
     * @return true 包含
     */
    public static boolean contains(String name) {
        return PLUGIN_HOLD.containsKey(name);
    }

    /**
     * 插件数量
     *
     * @return 当前加载的插件数量
     */
    public static int size() {
        return PLUGIN_HOLD.size();
    }

    /**
     * 正式环境添加依赖
     */
    private static void init() {
        File runPath = JpomManifest.getRunPath().getParentFile();
        File plugin = FileUtil.file(runPath, "plugin");
        if (!plugin.exists() || plugin.isFile()) {
            return;
        }
        // 加载二级插件包
        File[] dirFiles = plugin.listFiles(File::isDirectory);
        if (dirFiles != null) {
            for (File file : dirFiles) {
                File lib = FileUtil.file(file, "lib");
                if (!lib.exists() || lib.isFile()) {
                    continue;
                }
                File[] listFiles = lib.listFiles((dir, name) -> StrUtil.endWith(name, FileUtil.JAR_FILE_EXT, true));
                if (listFiles == null || listFiles.length == 0) {
                    continue;
                }
                addPlugin(file.getName(), lib);
            }
        }
        // 加载一级独立插件端包
        File[] files = plugin.listFiles(pathname -> FileUtil.isFile(pathname) && FileUtil.JAR_FILE_EXT.equalsIgnoreCase(FileUtil.extName(pathname)));
        if (files != null) {
            for (File file : files) {
                addPlugin(file.getName(), file);
            }
        }
    }

    private static void addPlugin(String pluginName, File file) {
        ClassLoader contextClassLoader = ClassLoaderUtil.getClassLoader();
        JarClassLoader.loadJar((URLClassLoader) contextClassLoader, file);
    }
}
