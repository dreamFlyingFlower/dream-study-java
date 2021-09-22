package com.wy.netty.study.timer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class S_ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("from server : ClassName - " + msg.getClass().getName() + " ; message : "
				+ msg.toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("client exceptionCaught method run...");
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * 当连接建立成功后触发,在一次连接中只运行唯一一次, 通常用于实现连接确认和资源初始化的
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client channel active");
	}
}