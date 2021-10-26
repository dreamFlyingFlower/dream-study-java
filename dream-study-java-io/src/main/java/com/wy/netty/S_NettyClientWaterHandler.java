package com.wy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Netty高水位处理
 * 
 * {@link DefaultChannelConfig#setWriteBufferHighWaterMark}:设置高水位
 * 
 * @author 飞花梦影
 * @date 2021-10-26 22:56:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_NettyClientWaterHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置高水位
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 设置高水位的最大值,默认是64KB
		ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
		// 当消息队列中的数据超过高水位时,队列中将不会再接收消息,此时消息不可写
		if (ctx.channel().isWritable()) {
			// 可写的处理逻辑
			ctx.writeAndFlush(Unpooled.wrappedBuffer("test".getBytes()));
		} else {
			// 消息队列满时的处理逻辑
			System.out.println("消息队列满了,处理不了");
		}
	}
}