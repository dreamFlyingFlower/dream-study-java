package com.wy.netty.study.protocol;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.wy.util.NettyUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 利用给定的协议解决粘包问题,协议格式为HEADcontent-length:xxxxHEADBODYxxxxxxBODY
 * 
 * @author 飞花梦影
 * @date 2021-09-02 23:37:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SuppressWarnings("resource")
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
				ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
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
			Scanner s = null;
			while (true) {
				s = new Scanner(System.in);
				System.out.print("enter message send to server > ");
				String line = s.nextLine();
				line = S_ClientHandler.ProtocolParser.transferTo(line);
				System.out.println("client send protocol content : " + line);
				future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes(StandardCharsets.UTF_8)));
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