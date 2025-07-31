package com.cowave.sys.meter.core.build.process;

/**
 * @author bwcx_jzy
 */
public class ReleaseProcess implements ProcessItem {

    @Override
    public String name() {
        return "Release";
    }

    @Override
    public Integer process() {
        return 0;
    }
}
