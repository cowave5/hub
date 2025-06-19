package com.cowave.sys.job.app.job;

import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.job.client.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
public class TestJob {

    @Job("test")
    public void test(){
        log.info("=====> {}", DateUtils.format("yyyy-mm-dd MM:HH:ss SSS"));
    }
}
