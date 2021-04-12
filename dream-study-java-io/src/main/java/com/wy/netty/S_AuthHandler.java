package com.wy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 * @author ParadiseWY
 * @date 2020-11-28 15:46:05
 * @git {@link https://github.com/mygodness100}
 */
public class S_AuthHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		if (paas(msg)) {
			ctx.pipeline().remove(this);
		} else {
			ctx.close();
		}
	}

	private boolean paas(ByteBuf password) {
		return false;
	}
}