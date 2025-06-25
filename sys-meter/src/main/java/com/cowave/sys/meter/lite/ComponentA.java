package com.cowave.sys.meter.lite;

import com.yomahub.liteflow.core.NodeComponent;

public class ComponentA extends NodeComponent {

    @Override
    public void process() throws Exception {
        System.out.println(1);
    }
}
