package com.wy.netty.barrage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 服务端逻辑处理
 *
 * @author 飞花梦影
 * @date 2021-11-09 08:53:02
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class BarrageServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 用于记录和管理所有客户端的channel
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// 获取客户端传输过来的消息
		String content = msg.text();
		System.err.println("收到消息：" + content);
		channels.writeAndFlush(new TextWebSocketFrame(content));
		System.err.println("写出消息完成：" + content);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		channels.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String channelId = ctx.channel().id().asShortText();
		System.out.println("客户端被移除,channelId为:" + channelId);
		channels.remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().close();
		channels.remove(ctx.channel());
	}
}