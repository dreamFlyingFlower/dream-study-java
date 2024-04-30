package com.wy.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 自定义心跳机制
 *
 * @author 飞花梦影
 * @date 2024-04-30 10:28:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				System.out.println("进入读空闲......");
			} else if (event.state() == IdleState.WRITER_IDLE) {
				System.out.println("进入写空闲......");
			} else if (event.state() == IdleState.ALL_IDLE) {
				System.out.println("channel 关闭之前");
				Channel channel = ctx.channel();
				// 资源释放
				channel.close();
				System.out.println("channel 关闭之后");
			}
		}
	}
}