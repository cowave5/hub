package com.cowave.sys.job.domain.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TriggerResponse {

    /**
     * 任务id
     */
    private int triggerId;

    /**
     * 日志id
     */
    private long logId;

    /**
     * 执行状态
     */
    private int handleStatus;

    /**
     * 执行开始时间
     */
    private Date handleTime;

    /**
     * 执行耗时
     */
    private long handleCost;

    /**
     * 失败原因
     */
    private String handleMsg;
}
