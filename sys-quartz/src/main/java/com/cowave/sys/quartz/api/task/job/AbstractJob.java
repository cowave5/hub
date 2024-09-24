/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.job;

import com.cowave.commons.tools.SpringContext;
import com.cowave.sys.quartz.api.task.entity.QuartzLog;
import com.cowave.sys.quartz.api.task.entity.QuartzTask;
import com.cowave.sys.quartz.api.task.service.TaskLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 示例模板
 *
 * @author shanhuiming
 */
public abstract class AbstractJob implements Job {

    private static final ThreadLocal<Date> JOB_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        QuartzTask quartzTask = (QuartzTask)context.getMergedJobDataMap().get(QuartzTask.JOB_PROPERTIES);
        try {
            before(context, quartzTask);
            doExecute(context, quartzTask);
            after(context, quartzTask, null);
        } catch (Exception e) {
            after(context, quartzTask, e);
        }
    }

    protected void before(JobExecutionContext context, QuartzTask quartzTask) {
        JOB_LOCAL.set(new Date());
    }

    protected abstract void doExecute(JobExecutionContext context, QuartzTask quartzTask) throws Exception;

    protected void after(JobExecutionContext context, QuartzTask quartzTask, Exception e) {
        Date beginTime = JOB_LOCAL.get();
        JOB_LOCAL.remove();
        Date endTime = new Date();
        QuartzLog quartzLog = new QuartzLog();
        quartzLog.setTaskId(quartzTask.getId());
        quartzLog.setBeginTime(beginTime);
        quartzLog.setEndTime(endTime);
        quartzLog.setCostTime(endTime.getTime() - beginTime.getTime());
        if (e != null) {
            quartzLog.setStatus(0);
            Throwable throwable;
            Throwable cause;
            for(throwable = e; (cause = throwable.getCause()) != null; throwable = cause) {}
            StringBuilder builder = (new StringBuilder(throwable.toString())).append(":<br>");
            StackTraceElement[] var12 = throwable.getStackTrace();
            int var13 = var12.length;
            for(int var14 = 0; var14 < var13; ++var14) {
                StackTraceElement stack = var12[var14];
                builder.append(stack).append("<br>");
            }
            quartzLog.setThrowable(builder.toString());
        } else {
            quartzLog.setStatus(1);
        }
        SpringContext.getBean(TaskLogService.class).add(quartzLog);
    }

    public static void invokeMethod(QuartzTask quartzTask) throws Exception {
        String invokeTarget = quartzTask.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);
        if (!isValidClassName(beanName)) {
            Object bean = SpringContext.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        } else {
            Object bean = Class.forName(beanName).getDeclaredConstructor().newInstance();
            invokeMethod(bean, methodName, methodParams);
        }
    }

    private static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    private static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    private static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return null;
        }
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        List<Object[]> classs = new LinkedList<>();
        for (String methodParam : methodParams) {
            String str = StringUtils.trimToEmpty(methodParam);
            if (StringUtils.startsWithAny(str, "'", "\"")) {
                // String字符串类型，以'或"开头
                classs.add(new Object[]{StringUtils.substring(str, 1, str.length() - 1), String.class});
            } else if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                // boolean布尔类型，等于true或者false
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            } else if (StringUtils.endsWith(str, "L")) {
                // long长整形，以L结尾
                classs.add(new Object[]{Long.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Long.class});
            } else if (StringUtils.endsWith(str, "D")) {
                // double浮点类型，以D结尾
                classs.add(new Object[]{Double.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Double.class});
            } else {
                // 其他类型归类为整形
                classs.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classs;
    }

    private static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }

    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) throws Exception {
        if (CollectionUtils.isNotEmpty(methodParams)) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }
    }

    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = os[0];
            index++;
        }
        return classs;
    }
}
