/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.socketio;

import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Configuration
public class SysSocketConfiguration {
    public static final String SPACE_NOTICE = "/notice";
    public static final String EVENT_CLIENT_NOTICE_COUNT = "getNoticeCount";

    public static final String EVENT_SERVER_NOTICE_COUNT = "noticeCount";
    public static final String EVENT_SERVER_NOTICE_NEW = "newNotice";

    private final SocketIoHelper socketIoHelper;
    private final GetNoticeCountEvent getNoticeCountEvent;

    @PostConstruct
    public void init(){
        socketIoHelper.registerConnectListener(SPACE_NOTICE);
        socketIoHelper.registerDisconnectListener(SPACE_NOTICE);
        socketIoHelper.registerDataListener(SPACE_NOTICE, EVENT_CLIENT_NOTICE_COUNT, String.class, getNoticeCountEvent);
    }
}
