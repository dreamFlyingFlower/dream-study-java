package com.wy.netty.barrage;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 利用Netty和WebSocket实现的弹幕功能
 *
 * @author 飞花梦影
 * @date 2021-11-09 08:48:41
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class BarrageServer {

	private EventLoopGroup mainGroup = new NioEventLoopGroup();

	private EventLoopGroup subGroup = new NioEventLoopGroup();

	private ServerBootstrap server = new ServerBootstrap();

	public BarrageServer() {
		server.group(mainGroup, subGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new HttpServerCodec());
						pipeline.addLast(new ChunkedWriteHandler());
						pipeline.addLast(new HttpObjectAggregator(1024 * 64));
						pipeline.addLast(new IdleStateHandler(8, 10, 12));
						pipeline.addLast(new WebSocketServerProtocolHandler("/barrage"));
						pipeline.addLast(new BarrageServerHandler());
					}
				});
		try {
			server.bind(9123).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}