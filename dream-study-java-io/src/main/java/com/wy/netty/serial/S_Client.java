/**
 * 1. 单线程组 2. Bootstrap配置启动信息 3. 注册业务处理Handler 4. connect连接服务，并发起请求
 */
package com.wy.netty.serial;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.wy.io.ZipTool;
import com.wy.model.NtClient;
import com.wy.util.MarshallingUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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

	public ChannelFuture doRequest(String host, int port, final ChannelHandler... handlers)
			throws InterruptedException {
		this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingDecoder());
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingEncoder());
				ch.pipeline().addLast(handlers);
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
			future = client.doRequest("localhost", 9999, new S_ClientHandler());
			String attachment = "test attachment";
			byte[] attBuf = attachment.getBytes();
			attBuf = ZipTool.zip(attBuf);
			NtClient msg = new NtClient(new Random().nextLong(), "test", attBuf);
			future.channel().writeAndFlush(msg);
			TimeUnit.SECONDS.sleep(1);
			future.addListener(ChannelFutureListener.CLOSE);
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