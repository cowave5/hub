package com.cowave.sys.job.client.netty;

import com.cowave.sys.job.client.ClientRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.*;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Slf4j
public class JobClient {

    private final MsgHandler msgHandler = new MsgHandlerImpl();

    private final ClientRegister clientRegister;

    private Thread clientThread;

    public void start(String clientIp, int clientPort, String clientName, String accessToken, Collection<String> handlerList) {
        clientThread = new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ThreadPoolExecutor clientPool = new ThreadPoolExecutor(0, 200, 60L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000),
                    r -> new Thread(r, "job-client-Pool-" + r.hashCode()),
                    (r, executor) -> {
                        throw new RuntimeException("job EXHAUSTED");
                    });

            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) {
                                channel.pipeline()
                                        .addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS))  // beat 3N, close if idle
                                        .addLast(new HttpServerCodec())
                                        .addLast(new HttpObjectAggregator(5 * 1024 * 1024))  // merge request & reponse to FULL
                                        .addLast(new JobClientHandler(clientPool, msgHandler, accessToken));
                            }
                        })
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                // 绑定端口
                ChannelFuture future = bootstrap.bind(clientPort).sync();
                log.info("job client start port: {}", clientPort);

                // start registry
                clientRegister.start(clientName, clientIp, clientPort, handlerList);

                // wait util stop
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.info("job client stop");
            } catch (Throwable e) {
                log.error("job client error.", e);
            } finally {
                try {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                } catch (Throwable e) {
                    log.error("", e);
                }
            }
        });
        clientThread.setName("job-client");
        clientThread.setDaemon(true);
        clientThread.start();
    }

    public void stop() {
        if (clientThread != null && clientThread.isAlive()) {
            clientThread.interrupt();
        }
        clientRegister.toStop();
    }
}
