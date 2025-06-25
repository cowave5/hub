package com.cowave.sys.job.domain.enums;

import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum JobTriggerStatus {

    /**
     * 初始化
     */
    INIT(0),

    /**
     * 路由失败
     */
    ROUTE_FAIL(10),

    /**
     * 调用异常
     */
    HTTP_FAIL(20),

    /**
     * 调用失败
     */
    REMOTE_FAIL(30),

    /**
     * 调用成功
     */
    REMOTE_SUCCESS(40),

    /**
     * 执行超时
     */
    EXEC_OVERTIME(50),

    /**
     * 执行失败
     */
    EXEC_FAIL(60),

    /**
     * 执行成功
     */
    EXEC_SUCCESS(70);

    JobTriggerStatus(int status) {
        this.status = status;
    }

    private final int status;
}
