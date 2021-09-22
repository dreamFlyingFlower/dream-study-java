package com.wy.netty.study.hearbeat;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳检测处理类
 *
 * @author 飞花梦影
 * @date 2021-09-18 14:06:13
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_ServerHandler01 extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg);
		// 上下2句最终调用的是同一个方法
		ctx.channel().writeAndFlush("hi");
		ctx.writeAndFlush("hi");
	}

	// 可以通过检测事件类型来达到检测心跳事件的目的
	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
		// 如果是心跳检测事件,并且即没有写也没有读,就关闭
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {

				// 清除超时会话
				ChannelFuture writeAndFlush = ctx.writeAndFlush("you will close");
				writeAndFlush.addListener(new ChannelFutureListener() {

					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						ctx.channel().close();
					}
				});
			}
		} else {
			// 其他事件按原来的逻辑执行
			super.userEventTriggered(ctx, evt);
		}
	}

	/**
	 * 新客户端接入
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
	}

	/**
	 * 客户端断开
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
	}

	/**
	 * 异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}