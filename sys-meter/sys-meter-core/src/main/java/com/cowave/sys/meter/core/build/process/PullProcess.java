package com.cowave.sys.meter.core.build.process;

/**
 * @author bwcx_jzy
 */
public class PullProcess implements ProcessItem {

    @Override
    public String name() {
        return "Pull";
    }

    @Override
    public Integer process() {
        return 0;
    }
}
