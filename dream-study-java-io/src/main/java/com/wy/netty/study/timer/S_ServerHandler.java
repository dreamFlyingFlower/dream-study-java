package com.wy.netty.study.timer;

import com.wy.model.NtServer;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * {@link Sharable}:代表当前Handler是一个可以分享的处理器.即服务器注册此Handler后,可以分享给多个客户端同时使用. 如果不使用注解描述类型,则每次客户端请求时,必须为客户端重新创建一个新的Handler对象
 * 
 * @author 飞花梦影
 * @date 2021-09-02 23:24:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Sharable
public class S_ServerHandler extends ChannelInboundHandlerAdapter {

	// 业务处理逻辑
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("from client : ClassName - " + msg.getClass().getName() + " ; message : " + msg.toString());
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