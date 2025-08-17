package com.cowave.sys.meter.core.build.process;

/**
 * @author bwcx_jzy
 */
public class ReadyProcess implements ProcessItem {

    @Override
    public String name() {
        return "Ready";
    }

    @Override
    public Integer process() {
        return 0;
    }
}
