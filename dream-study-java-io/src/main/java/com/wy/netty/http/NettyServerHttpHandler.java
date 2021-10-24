package com.wy.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 服务端处理类,处理http请求,接收FullHttpRequest
 * 
 * @author 飞花梦影
 * @date 2021-10-23 16:58:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyServerHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
		if (!fullHttpRequest.decoderResult().isSuccess()) {
			sendError(ctx, "400");
			return;
		}
		ByteBuf byteBuf = fullHttpRequest.content().copy();
		FullHttpResponse fullHttpResponse =
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
		fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
		ctx.writeAndFlush(fullHttpResponse).sync();
	}

	private void sendError(ChannelHandlerContext ctx, String string) {
		ctx.channel().writeAndFlush(string);
	}
}