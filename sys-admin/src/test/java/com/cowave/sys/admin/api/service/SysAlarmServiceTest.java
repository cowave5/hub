/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.cowave.sys.admin.SpringTest;
import com.cowave.sys.admin.api.service.impl.SysAlarmServiceImpl;
import com.cowave.sys.model.admin.SysAlarm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author shanhuiming
 *
 */
public class SysAlarmServiceTest extends SpringTest {

    @Autowired
    private SysAlarmServiceImpl sysAlarmService;

    @Test
    public void accept() {
        SysAlarm sysAlarm = new SysAlarm();
        sysAlarm.setAlarmCode("xxx");
        sysAlarmService.handle(sysAlarm);
    }
}
