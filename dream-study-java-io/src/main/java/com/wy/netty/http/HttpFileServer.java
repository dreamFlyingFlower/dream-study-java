package com.wy.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * http协议文件传输
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
		new HttpFileServer(port).run();// 启动服务
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// 线程一 //这个是用于serversocketchannel的event
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 线程二//这个是用于处理accept到的channel
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