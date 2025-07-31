package com.cowave.sys.meter.core.plugin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bwcx_jzy
 */
public interface IPlugin extends AutoCloseable {

    String DATE_PATH_KEY = "JPOM_DATE_PATH";

    String JPOM_VERSION_KEY = "JPOM_VERSION";

    Object execute(Object var1, Map<String, Object> var2) throws Exception;

    default Object execute(Object main, Object... parameters) throws Exception {
        int length = parameters.length;
        Map<String, Object> map = new HashMap(length / 2);

        for(int i = 0; i < length; i += 2) {
            map.put(parameters[i].toString(), parameters[i + 1]);
        }

        return this.execute(main, map);
    }

    default <T> T execute(Object main, Class<T> cls, Object... parameters) throws Exception {
        Object execute = this.execute(main, parameters);
        return (T)this.convertResult(execute, cls);
    }

    default <T> T execute(Object main, Map<String, Object> parameter, Class<T> cls) throws Exception {
        Object execute = this.execute(main, parameter);
        return (T)this.convertResult(execute, cls);
    }

    default <T> T convertResult(Object execute, Class<T> cls) {
        if (execute == null) {
            return null;
        } else {
            Class<?> aClass = execute.getClass();
            if (ClassUtil.isSimpleValueType(aClass)) {
                return (T) Convert.convert(aClass, execute);
            } else if (execute instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject)execute;
                return (T)jsonObject.to(cls, new JSONReader.Feature[0]);
            } else {
                return (T)execute;
            }
        }
    }

    default void close() throws Exception {

    }
}
