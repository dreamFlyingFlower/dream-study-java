package com.wy.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 字符串信息处理
 *
 * @author 飞花梦影
 * @date 2021-09-18 10:53:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_NettyServer1Handler extends SimpleChannelInboundHandler<String> {

	/**
	 * 新客户端接入
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("客户端新建链接");
	}

	/**
	 * 客户端断开链接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端断开链接");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		System.out.println("channelRegistered");
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		System.out.println("handlerAdded");
	}

	/**
	 * 服务端异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	/**
	 * 处理从上一个handler中传过来的参数,此处是String,则可以直接接收String类型 如果上一个管道并没有指定任何类型,接收的原始参数类型是PooledUnsafeDirectByteBuf或其他ByteBuf的字类
	 * 
	 * @param ctx 上下文环境
	 * @param msg 上一个handler传递的字符串信息
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg.getClass());
		System.out.println(msg);
		ctx.writeAndFlush("实际上和下面的方法调用的是相同的实现");
		ctx.channel().writeAndFlush("回写数据");
	}
}