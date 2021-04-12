package com.wy.netty.timer;

import com.wy.model.NtServer;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class S_ServerHandler extends ChannelInboundHandlerAdapter {

	// 业务处理逻辑
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("from client : ClassName - " + msg.getClass().getName() + " ; message : "
				+ msg.toString());
		NtServer response = new NtServer(0L, "test response");
		ctx.writeAndFlush(response);
	}

	// 异常处理逻辑
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("server exceptionCaught method run...");
		// cause.printStackTrace();
		ctx.close();
	}

}
