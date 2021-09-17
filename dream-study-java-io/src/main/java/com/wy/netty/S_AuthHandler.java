package com.wy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 数据处理类,只需要重写channelRead0()即可,该方法的参数类型来自于上一个管道的返回类型
 * 
 * @author 飞花梦影
 * @date 2020-11-28 15:46:05
 * @git {@link https://github.com/dreamFlyingFlower}
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