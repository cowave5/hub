package com.cowave.sys.job.client;

import com.cowave.commons.client.http.annotation.HttpClient;
import com.cowave.commons.client.http.register.HttpProxyFactory;
import com.cowave.sys.job.client.handler.JobHandler;
import com.cowave.sys.job.client.handler.MethodJobHandler;
import com.cowave.sys.job.client.netty.JobClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Data
public class JobContext implements ApplicationContextAware, EmbeddedValueResolverAware, SmartInitializingSingleton, DisposableBean {

    private static ConcurrentMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<>();

    public static JobThread getJobThread(int jobId) {
        return jobThreadRepository.get(jobId);
    }

    public static JobThread putJobThread(int jobId, JobHandler handler) {
        JobThread currentJobThread = new JobThread(jobId, handler);
        currentJobThread.start();

        JobThread lastJobThread = jobThreadRepository.put(jobId, currentJobThread);
        if (lastJobThread != null) {
            lastJobThread.toStop();
            lastJobThread.interrupt();
        }
        return currentJobThread;
    }

    public static JobThread delJobThread(int jobId) {
        JobThread lastJobThread = jobThreadRepository.remove(jobId);
        if (lastJobThread != null) {
            lastJobThread.toStop();
            lastJobThread.interrupt();
            return lastJobThread;
        }
        return null;
    }

    private static ConcurrentMap<String, JobHandler> jobHandlerRepository = new ConcurrentHashMap<>();

    public static JobHandler getJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }

    public static JobHandler putJobHandler(String name, JobHandler jobHandler) {
        return jobHandlerRepository.put(name, jobHandler);
    }

    protected void registJobHandler(Job job, Object bean, Method method) {
        if (job == null) {
            return;
        }

        Class<?> clazz = bean.getClass();
        String methodName = method.getName();

        String name = job.value();
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("job name can't be empty, [" + clazz + "#" + methodName + "]");
        }

        if (getJobHandler(name) != null) {
            throw new RuntimeException("job name [" + name + "] conflicts");
        }

        method.setAccessible(true);
        Method initMethod = null;
        Method destroyMethod = null;

        if (StringUtils.isNotBlank(job.init())) {
            try {
                initMethod = clazz.getDeclaredMethod(job.init());
                initMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("job initMethod invalid, for[" + clazz + "#" + methodName + "]");
            }
        }

        if (StringUtils.isNotBlank(job.destroy())) {
            try {
                destroyMethod = clazz.getDeclaredMethod(job.destroy());
                destroyMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("job destroyMethod invalid, for[" + clazz + "#" + methodName + "]");
            }
        }
        putJobHandler(name, new MethodJobHandler(bean, method, initMethod, destroyMethod));
    }

    private ApplicationContext applicationContext;

    private StringValueResolver valueResolver;

    private ServerService serverService;

    private JobClient jobClient;

    private ClientCallbackHandler clientCallbackHandler;

    private List<String> serverList;

    private String clinetName;

    private String clientIp;

    private int clientPort;

    private String accessToken;

    private int timeout;

    private String logPath;

    private int logRetentionDays;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        setServerService();
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.valueResolver = valueResolver;
        setServerService();
    }

    private void setServerService() {
        if(this.applicationContext == null || this.valueResolver == null){
            return;
        }
        try {
            this.serverService = HttpProxyFactory.newProxy(
                ServerService.class, ServerService.class.getAnnotation(HttpClient.class), applicationContext, valueResolver);
        }catch (Exception e){
            log.error("", e);
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 初始化job handler
        initJobHandlerMethodRepository(applicationContext);

        // client回调
        clientCallbackHandler = new ClientCallbackHandler(serverService, this.serverList);
        clientCallbackHandler.start();

        ClientRegister clientRegister = new ClientRegister(this.serverService, this.serverList);

        // 启动客户端监听
        jobClient = new JobClient(clientRegister);
        jobClient.start(clientIp, clientPort, clinetName, accessToken, jobHandlerRepository.keySet());
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean;
            Lazy lazy = applicationContext.findAnnotationOnBean(beanDefinitionName, Lazy.class);
            if (lazy!=null){
                continue;
            }else {
                bean = applicationContext.getBean(beanDefinitionName);
            }

            Map<Method, Job> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                    (MethodIntrospector.MetadataLookup<Job>)method -> AnnotatedElementUtils.findMergedAnnotation(method, Job.class));
            if (annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, Job> entry : annotatedMethods.entrySet()) {
                Method executeMethod = entry.getKey();
                Job job = entry.getValue();
                registJobHandler(job, bean, executeMethod);
            }
        }
    }

    @Override
    public void destroy() {
        if (jobClient != null) {
            try {
                jobClient.stop();
            } catch (Exception e) {
                log.error("", e);
            }
        }

        if (!jobThreadRepository.isEmpty()) {
            for (Map.Entry<Integer, JobThread> item : jobThreadRepository.entrySet()) {
                JobThread oldJobThread = delJobThread(item.getKey());
                if (oldJobThread != null) {
                    try {
                        // wait for job thread
                        oldJobThread.join();
                    } catch (InterruptedException e) {
                        log.error("destroy join interrupted, {}", item.getKey(), e);
                    }
                }
            }
            jobThreadRepository.clear();
        }

        jobHandlerRepository.clear();

        if(clientCallbackHandler != null){
            clientCallbackHandler.toStop();
        }
    }
}
