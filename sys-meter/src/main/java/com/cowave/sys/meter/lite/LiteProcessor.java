package com.cowave.sys.meter.lite;

import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LiteProcessor {

    @Resource
    private FlowExecutor flowExecutor;

    public LiteflowResponse xx() {
        LiteFlowNodeBuilder.createCommonNode()
                .setId("a")
                .setName("组件A")
                .setClazz("com.cowave.sys.meter.lite.ComponentA")
                .build();

        LiteFlowChainELBuilder.createChain().setChainId("chain2").setEL(
                "THEN(a)"
        ).build();

        return flowExecutor.execute2Resp("chain2", "arg");
    }
}
