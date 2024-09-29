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

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.cowave.sys.admin.infra.base.dao.SysNoticeUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cowave.sys.admin.infra.base.socketio.SysSocketConfiguration.EVENT_SERVER_NOTICE_COUNT;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
public class GetNoticeCountEvent implements DataListener<String> {

    private final SysNoticeUserDao sysNoticeUserDao;

    @Override
    public void onData(SocketIOClient client, String userCode, AckRequest ackSender) {
        Long noticeCount = sysNoticeUserDao.countUnReadByUserId(userCode);
        client.sendEvent(EVENT_SERVER_NOTICE_COUNT, noticeCount);
    }
}
