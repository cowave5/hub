package com.cowave.sys.meter.core.build.process;

/**
 * @author bwcx_jzy
 */
public class FinishProcess implements ProcessItem {

    @Override
    public String name() {
        return "Finish";
    }

    @Override
    public Integer process() {
        return 0;
    }
}
