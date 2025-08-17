package com.cowave.sys.meter.core.plugin;

import com.cowave.sys.meter.core.utils.ExtConfigFile;

import java.io.InputStream;

/**
 * @author bwcx_jzy
 */
public interface IDefaultPlugin extends IPlugin, AutoCloseable {

    /**
     * 获取配置文件流
     */
    default InputStream getConfigResourceInputStream(String name) {
        return ExtConfigFile.tryGetConfigResourceInputStream(name);
    }
}
