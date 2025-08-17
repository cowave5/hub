/*
 * Copyright (c) 2019 Of Him Code Technology Studio
 * Jpom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * 			http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package com.cowave.sys.meter.plugin.docker.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Optional;

@Slf4j
class JschSocket extends Socket {
    private final Session session;
    private final boolean useSudo;
    private Channel channel;
    private InputStream inputStream;
    private OutputStream outputStream;

    JschSocket(Session session, boolean useSudo) {
        this.session = session;
        this.useSudo = useSudo;
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        connect(0);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        connect(timeout);
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public boolean isClosed() {
        return channel != null && channel.isClosed();
    }

    private void connect(int timeout) throws IOException {
        try {
            // only 18.09 and up
            channel = session.openChannel("exec");
            String command;
            if (useSudo) {
                command = "sudo docker system dial-stdio";
            } else {
                command = "docker system dial-stdio";
            }
            ((ChannelExec) channel).setCommand(command);
            log.debug("Using dialer command【{}】", command);
            inputStream = channel.getInputStream();
            outputStream = channel.getOutputStream();
            channel.connect(timeout);
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }

    @Override
    public synchronized void close() throws IOException {
        Optional.ofNullable(channel).ifPresent(Channel::disconnect);
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
