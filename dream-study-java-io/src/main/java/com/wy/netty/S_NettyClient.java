package com.wy.netty;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.wy.util.NettyUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty客户端,单线程组,利用Bootstrap配置启动,需要注册业务Handler
 * 
 * @apiNote 因为客户端是请求的发起者,不需要监听,只需要定义唯一的一个线程组即可
 *
 * @author ParadiseWY
 * @date 2019-05-13 18:58:27
 * @git {@link https://github.com/mygodness100}
 */
@SuppressWarnings("resource")
public class S_NettyClient {

	// 处理请求和处理服务端响应的线程组
	private EventLoopGroup group = null;

	// 客户端启动相关配置信息
	private Bootstrap bootstrap = null;

	public S_NettyClient() {
		init();
	}

	private void init() {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		// 绑定线程组
		bootstrap.group(group);
		// 设定通讯模式为NIO
		bootstrap.channel(NioSocketChannel.class);
	}

	public ChannelFuture doRequest(String host, int port, final ChannelHandler... handlers)
			throws InterruptedException {
		/*
		 * 客户端的Bootstrap没有childHandler方法,只有handler().
		 * 该方法含义等同ServerBootstrap中的childHandler().在客户端必须绑定处理器,也就是必须调用handler()
		 * 服务器必须绑定处理器,必须调用childHandler()
		 */
		this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(handlers);
			}
		});
		// 建立连接
		ChannelFuture future = this.bootstrap.connect(host, port).sync();
		return future;
	}

	public void release() {
		this.group.shutdownGracefully();
	}

	public static void main(String[] args) {
		S_NettyClient client = null;
		ChannelFuture future = null;
		try {
			client = new S_NettyClient();
			future = client.doRequest("localhost", 8888, new S_ClientHandler());
			Scanner s = null;
			while (true) {
				s = new Scanner(System.in);
				System.out.print("enter message send to server (enter 'exit' for close client) > ");
				String line = s.nextLine();
				if ("exit".equals(line)) {
					// addListener - 增加监听,当某条件满足的时候,触发监听器
					// ChannelFutureListener.CLOSE - 关闭监听器,代表ChannelFuture执行返回后,关闭连接
					future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("UTF-8")))
							.addListener(ChannelFutureListener.CLOSE);
					break;
				}
				future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("UTF-8")));
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			NettyUtils.closeFuture(future);
			if (null != client) {
				client.release();
			}
		}
	}
}