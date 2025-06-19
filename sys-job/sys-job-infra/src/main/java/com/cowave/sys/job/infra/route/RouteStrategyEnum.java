package com.cowave.sys.job.infra.route;

import com.cowave.commons.tools.EnumVal;
import com.cowave.sys.job.infra.route.impl.*;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum RouteStrategyEnum implements EnumVal<Void> {

    /**
     * 第一个
     */
    FIRST(new ClientRouteFirst()),

    /**
     * 最后一个
     */
    LAST(new ClientRouteLast()),

    /**
     * 轮询
     */
    ROUND(new ClientRouteRound()),

    /**
     * 随机
     */
    RANDOM(new ClientRouteRandom()),

    /**
     * HASH散列
     */
    CONSISTENT_HASH(new ClientRouteConsistentHash()),

    /**
     * 最不经常使用
     */
    LEAST_FREQUENTLY_USED(new ClientRouteLFU()),

    /**
     * 最久未使用
     */
    LEAST_RECENTLY_USED(new ClientRouteLRU()),

    /**
     * 故障转移
     */
    FAIL_OVER(new ClientRouteFailover()),

    /**
     * 忙碌转移
     */
    BUSY_OVER(new ClientRouteBusyOver()),

    /**
     * 分片广播
     */
    SHARDING_BROADCAST(null);

    RouteStrategyEnum(ClientRouter router) {
        this.router = router;
    }

    private final ClientRouter router;
}
