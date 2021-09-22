package com.wy.netty;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端逻辑处理
 * 
 * @author ParadiseWY
 * @date 2020-11-28 15:42:53
 * @git {@link https://github.com/mygodness100}
 */
public class S_ServerHandler1 extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 耗时的操作
				String result = loadFromDB();
				ctx.channel().writeAndFlush(result);
				ctx.executor().schedule(new Runnable() {

					@Override
					public void run() {
						// ...
					}
				}, 1, TimeUnit.SECONDS);
			}
		}).start();
	}

	private String loadFromDB() {
		return "hello world!";
	}
}