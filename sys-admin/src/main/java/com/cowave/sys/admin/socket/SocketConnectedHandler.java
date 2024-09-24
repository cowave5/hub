/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.socket;

import com.cowave.commons.framework.helper.socketio.ConnectedHandler;
import com.cowave.commons.framework.helper.socketio.SocketServer;
import com.cowave.sys.admin.api.mapper.SysNoticeMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Component
public class SocketConnectedHandler implements ConnectedHandler {

    private final SysNoticeMapper sysNoticeMapper;

    @Override
    public void onConnected(Long userId, SocketServer socketServer) {
        int unread = sysNoticeMapper.countUserUnRead(userId);
        socketServer.sendSingle("notice_unread", unread, userId);
    }
}
