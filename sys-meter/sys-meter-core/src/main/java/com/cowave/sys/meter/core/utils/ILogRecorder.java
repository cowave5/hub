package com.cowave.sys.meter.core.utils;

/**
 * @author bwcx_jzy
 */
public interface ILogRecorder {

    String info(String var1, Object... var2);

    String system(String var1, Object... var2);

    String systemError(String var1, Object... var2);

    String systemWarning(String var1, Object... var2);
}
