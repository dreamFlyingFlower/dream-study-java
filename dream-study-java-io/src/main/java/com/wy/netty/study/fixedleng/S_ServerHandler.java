package com.wy.netty.study.fixedleng;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class S_ServerHandler extends ChannelInboundHandlerAdapter {

	// 业务处理逻辑
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String message = msg.toString();
		System.out.println("from client : " + message.trim());
		String line = "ok ";
		ctx.writeAndFlush(Unpooled.copiedBuffer(line.getBytes("UTF-8")));
	}

	// 异常处理逻辑
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("server exceptionCaught method run...");
		// cause.printStackTrace();
		ctx.close();
	}
}