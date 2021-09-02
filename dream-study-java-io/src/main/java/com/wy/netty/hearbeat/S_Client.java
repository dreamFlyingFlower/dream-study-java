package com.wy.netty.hearbeat;

import com.wy.util.MarshallingUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 心跳监测,用于监测电脑硬件和软件信息的一种技术
 * 
 * @author 飞花梦影
 * @date 2021-09-02 17:32:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_Client {

	// 处理请求和处理服务端响应的线程组
	private EventLoopGroup group = null;

	// 服务启动相关配置信息
	private Bootstrap bootstrap = null;

	public S_Client() {
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

	public ChannelFuture doRequest(String host, int port) throws InterruptedException {
		this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingDecoder());
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingEncoder());
				ch.pipeline().addLast(new S_ClientHandler());
			}
		});
		ChannelFuture future = this.bootstrap.connect(host, port).sync();
		return future;
	}

	public void release() {
		this.group.shutdownGracefully();
	}

	public static void main(String[] args) {
		S_Client client = null;
		ChannelFuture future = null;
		try {
			client = new S_Client();
			future = client.doRequest("localhost", 9999);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != future) {
				try {
					future.channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (null != client) {
				client.release();
			}
		}
	}
}