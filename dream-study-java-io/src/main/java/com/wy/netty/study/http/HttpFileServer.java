package com.wy.netty.study.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * http协议文件传输
 * 
 * @author 飞花梦影
 * @date 2021-09-02 23:44:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HttpFileServer {

	private final int port;

	public HttpFileServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		int port = 8089;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8089;
		}
		new HttpFileServer(port).run();
	}

	public void run() throws Exception {
		// 线程一,用于serversocketchannel的event
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 线程二,用于处理accept到的channel
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new HttpFileServerInitializer());
			b.bind(port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}