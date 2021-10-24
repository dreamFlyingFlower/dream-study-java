package com.wy.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 模拟Http请求体body获取异常的服务端
 * 
 * @author 飞花梦影
 * @date 2021-10-23 16:44:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyServerHttp {

	public static void main(String[] args) {
		NettyServerHttp nettyServerHttp = new NettyServerHttp();
		nettyServerHttp.init(9999);
	}

	private void init(int port) {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workGroup = new NioEventLoopGroup(1);
		ServerBootstrap server = new ServerBootstrap();
		server.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 对get请求在URL后面的参数进行解码
						ch.pipeline().addLast(new HttpServerCodec());
						// 对post请求参数进行解码
						ch.pipeline().addLast(new HttpObjectAggregator(Short.MAX_VALUE));
						// 业务handler
						ch.pipeline().addLast(new NettyServerHttpHandler());
					}
				}).option(ChannelOption.SO_BACKLOG, 128);
		try {
			ChannelFuture future = server.bind("127.0.0.1", port).sync();
			future.channel().closeFuture().sync();
			future.channel().closeFuture().addListener(p -> {
				bossGroup.shutdownGracefully();
				workGroup.shutdownGracefully();
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}