package com.cowave.sys.job.client.netty;

import com.alibaba.fastjson.JSON;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.IdleCheck;
import com.cowave.sys.job.domain.client.KillRequest;
import com.cowave.sys.job.domain.client.TriggerRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
public class JobClientHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final ThreadPoolExecutor clientPool;

    private final MsgHandler msgHandler;

    private final String accessToken;

    public JobClientHandler(ThreadPoolExecutor clientPool, MsgHandler msgHandler, String accessToken) {
        this.clientPool = clientPool;
        this.msgHandler = msgHandler;
        this.accessToken = accessToken;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest msg) {
        String requestData = msg.content().toString(CharsetUtil.UTF_8);
        String uri = msg.uri();
        HttpMethod httpMethod = msg.method();
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        String accessToken = msg.headers().get("Job-Token");
        clientPool.execute(() -> {
            Object responseObj = process(httpMethod, uri, requestData, accessToken);
            writeResponse(ctx, keepAlive, JSON.toJSONString(responseObj));
        });
    }

    private Object process(HttpMethod httpMethod, String uri, String requestData, String accessTokenReq) {
        if (HttpMethod.POST != httpMethod) {
            return Response.error("invalid request, HttpMethod not support.");
        }

        if (StringUtils.isBlank(uri)) {
            return Response.error("invalid request, uri-mapping empty.");
        }

        if (StringUtils.isNotBlank(accessToken) && !accessToken.equals(accessTokenReq)) {
            return Response.error("invalid access token.");
        }

        try {
            return switch (uri) {
                case "/beat" -> msgHandler.beat();
                case "/idle" -> msgHandler.checkIdle(JSON.parseObject(requestData, IdleCheck.class));
                case "/exec" -> msgHandler.exec(JSON.parseObject(requestData, TriggerRequest.class));
                case "/kill" -> msgHandler.kill(JSON.parseObject(requestData, KillRequest.class));
                default -> Response.error("invalid request uri");
            };
        } catch (Throwable e) {
            log.error("", e);
            return Response.error(e.getMessage());
        }
    }

    private void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, String responseJson) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.copiedBuffer(responseJson, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("job client caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
