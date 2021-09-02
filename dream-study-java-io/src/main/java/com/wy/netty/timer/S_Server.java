package com.wy.netty.timer;

import com.wy.util.MarshallingUtils;
import com.wy.util.NettyUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 定时任务服务端
 * 
 * ReadTimeoutHandler处理定时任务,不是一个Shareable修饰的handler.所以每次都必须新建一个handler实例,否则可能会出现数据异常
 * 
 * @author 飞花梦影
 * @date 2021-09-02 23:27:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Server {

	// 监听线程组,监听客户端请求
	private EventLoopGroup acceptorGroup = null;

	// 处理客户端相关操作线程组,负责处理与客户端的数据通讯
	private EventLoopGroup clientGroup = null;

	// 服务启动相关配置信息
	private ServerBootstrap bootstrap = null;

	public static void main(String[] args) {
		ChannelFuture future = null;
		S_Server server = null;
		try {
			server = new S_Server();
			future = server.doAccept(9999);
			System.out.println("server started.");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			NettyUtils.closeFuture(future);
			if (null != server) {
				server.release();
			}
		}
	}

	public S_Server() {
		init();
	}

	private void init() {
		acceptorGroup = new NioEventLoopGroup();
		clientGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		// 绑定线程组
		bootstrap.group(acceptorGroup, clientGroup);
		// 设定通讯模式为NIO
		bootstrap.channel(NioServerSocketChannel.class);
		// 设定缓冲区大小
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		// SO_SNDBUF发送缓冲区,SO_RCVBUF接收缓冲区,SO_KEEPALIVE开启心跳监测(保证连接有效)
		bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024).option(ChannelOption.SO_RCVBUF, 16 * 1024)
				.option(ChannelOption.SO_KEEPALIVE, true);
		// 增加日志Handler,日志级别为info
		// bootstrap.handler(new LoggingHandler(LogLevel.INFO));
	}

	public ChannelFuture doAccept(int port) throws InterruptedException {
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingDecoder());
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingEncoder());
				// 定义一个定时断线处理器,指定时间内没有任何的可读取数据,自动断开连接,默认的单位是秒
				ch.pipeline().addLast(new ReadTimeoutHandler(3));
				// 自定义间隔时长单位,第一个是时长,第2个是单位
				// ch.pipeline().addLast(new ReadTimeoutHandler(3, TimeUnit.SECONDS));
				ch.pipeline().addLast(new S_ServerHandler());
			}
		});
		ChannelFuture future = bootstrap.bind(port).sync();
		return future;
	}

	public void release() {
		this.acceptorGroup.shutdownGracefully();
		this.clientGroup.shutdownGracefully();
	}
}