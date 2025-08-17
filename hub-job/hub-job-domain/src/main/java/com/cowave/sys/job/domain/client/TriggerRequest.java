package com.cowave.sys.job.domain.client;

import lombok.Data;


/**
 * @author xuxueli/shanhuiming
 */
@Data
public class TriggerRequest {

    /**
     * 任务id
     */
    private int triggerId;

    /**
     * 日志id
     */
    private long logId;

    /**
     * 执行器名称
     */
    private String handlerName;

    /**
     * 执行参数
     */
    private String handlerParams;

    /**
     * 阻塞策略
     */
    private String blockStrategy;

    /**
     * 超时时间
     */
    private int timeOut;

    /**
     * 任务类型
     */
    private String glueType;

    /**
     * 脚本内容
     */
    private String glueSource;

    /**
     * 脚本更新时间
     */
    private long glueUpdateTime;

    /**
     * 分片参数
     */
    private int broadIndex;

    /**
     * 分片参数
     */
    private int broadTotal;
}
