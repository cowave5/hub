package com.cowave.sys.meter.domain.torna.bean;

public interface IConfig {

    String getConfig(String key);

    String getConfig(String key, String defaultValue);

}
