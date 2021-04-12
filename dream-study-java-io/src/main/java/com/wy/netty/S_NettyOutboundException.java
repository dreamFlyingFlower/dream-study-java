package com.wy.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * 自定义Netty的outbound异常传播
 * 
 * @author ParadiseWY
 * @date 2020-11-29 11:08:34
 * @git {@link https://github.com/mygodness100}
 */
public class S_NettyOutboundException extends ChannelOutboundHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
}