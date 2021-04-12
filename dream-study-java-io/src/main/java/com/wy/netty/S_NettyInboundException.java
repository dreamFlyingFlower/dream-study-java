package com.wy.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Netty异常处理,自定义inbound的异常传播
 * 
 * @author ParadiseWY
 * @date 2020-11-29 11:03:07
 * @git {@link https://github.com/mygodness100}
 */
public class S_NettyInboundException extends ChannelInboundHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}