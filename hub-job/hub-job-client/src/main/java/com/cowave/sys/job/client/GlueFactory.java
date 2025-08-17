package com.cowave.sys.job.client;

import com.cowave.commons.tools.SpringContext;
import com.cowave.sys.job.client.handler.JobHandler;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
public class GlueFactory {

	public static GlueFactory instance = new GlueFactory();

	private final GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

	private final ConcurrentMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

	public JobHandler loadNewInstance(String codeSource) throws Exception{
		if (StringUtils.isNotBlank(codeSource)) {
			Class<?> clazz = getCodeSourceClass(codeSource);
			if (clazz != null) {
				Object instance = clazz.newInstance();
                if (instance instanceof JobHandler) {
                    this.injectService(instance);
                    return (JobHandler) instance;
                } else {
                    throw new IllegalArgumentException("cannot convert from instance[" + instance.getClass() + "] to JobHandler");
                }
            }
		}
		throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, instance is null");
	}

	private Class<?> getCodeSourceClass(String codeSource){
		try {
			// md5
			byte[] md5 = MessageDigest.getInstance("MD5").digest(codeSource.getBytes());
			String md5Str = new BigInteger(1, md5).toString(16);

			Class<?> clazz = CLASS_CACHE.get(md5Str);
			if(clazz == null){
				clazz = groovyClassLoader.parseClass(codeSource);
				CLASS_CACHE.putIfAbsent(md5Str, clazz);
			}
			return clazz;
		} catch (Exception e) {
			return groovyClassLoader.parseClass(codeSource);
		}
	}

    public void injectService(Object instance){
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object fieldBean = null;
            if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
                try {
                    Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
                    if (resource.name()!=null && resource.name().length()>0){
                        fieldBean = SpringContext.getBean(resource.name());
                    } else {
                        fieldBean = SpringContext.getBean(field.getName());
                    }
                } catch (Exception e) {

                }
                if (fieldBean==null ) {
                    fieldBean = SpringContext.getBean(field.getType());
                }
            } else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
                if (qualifier != null && !qualifier.value().isEmpty()) {
                    fieldBean = SpringContext.getBean(qualifier.value());
                } else {
                    fieldBean = SpringContext.getBean(field.getType());
                }
            }

            if (fieldBean!=null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldBean);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
