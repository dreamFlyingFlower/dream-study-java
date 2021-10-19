package com.wy.netty;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import com.wy.util.NettyUtils;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

/**
 * Netty客户端,请求的发起者,不需要监听.只需要定义一个线程组即可,利用Bootstrap配置启动,需要注册业务Handler
 * 
 * {@link EventLoopGroup}:底层也是一个线程池,不需要循环创建.若循环创建,可能会导致OOM
 * 
 * Netty客户端创建原理:
 * 
 * <pre>
 * 1.调用方创建客户端BootStrap
 * 2.构建NIO线程组EventLoopGroup
 * 3.通过反射创建NioSocketChannel
 * 4.NioSocketChannel创建默认的ChannelPipeLine
 * 5.NioSocketChannel异步发起TCP客户端连接
 * 6.注册OP_CONNECT事件到NIO线程多路复用器EventLoopGroup
 * 7.异步处理连接结果
 * 8.发送连接操作结果事件到ChannelPipeLine
 * 9.调用应用添加的业务ChannelHandler
 * </pre>
 * 
 * Netty客户端源码解析:
 * 
 * <pre>
 * {@link NioEventLoopGroup}:继承{@link MultithreadEventLoopGroup},构造线程组
 * {@link MultithreadEventExecutorGroup}:由 NioEventLoopGroup 构造函数最终调用而来,创建线程组
 * {@link ThreadPerTaskExecutor}: MultithreadEventExecutorGroup (75)创建的执行器{@link Executor}
 * {@link Bootstrap}:绑定线程组,设置非阻塞,设置相关参数,设置handler
 * {@link Bootstrap#connect()}:开始建立连接(155)
 * {@link AbstractBootstrap#initAndRegister}:建立连接时调用,进行通道的初始化和注册(307)
 * 
 * writeAndFlush申请的内存(堆内存和直接内存)都被Netty主动释放掉了,而会发生内存泄露的是接收数据的时候,
 * 即读数据进行分配时,若使用了Unpool等方式分配了内存空间,但是不释放内存空间,就可能引起内存泄露
 * </pre>
 *
 * @author 飞花梦影
 * @date 2019-05-13 18:58:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SuppressWarnings("resource")
public class S_NettyClient {

	/** 处理请求和处理服务端响应的线程组 */
	private EventLoopGroup group = null;

	/** 客户端启动相关配置信息 */
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
		return this.bootstrap.connect(host, port).sync();
	}

	public void release() {
		// 关闭整个线程组
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
			// 关闭当前线程
			NettyUtils.closeFuture(future);
			if (null != client) {
				client.release();
			}
		}
	}
}