package com.wy.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * AIO:异步非阻塞的编程方式,适用于高并发,长连接架构,如相册服务器.
 * 
 * AIO只需要调用read/write方法,他们是异步完成,无需等待,操作完成后会主动调用回调函数
 * 
 * @author 飞花梦影
 * @date 2019-04-23 11:45:22
 */
public class AioServer {

	private static ExecutorService pool = Executors.newFixedThreadPool(5);

	// 线程组
	// private AsynchronousChannelGroup group;

	// 服务器通道,针对服务器端定义的方法
	private AsynchronousServerSocketChannel serverChannel;

	public static void main(String[] args) {
		AioServer bean = new AioServer();
		bean.init(8889);
	}

	private void init(int port) {
		try {
			// 使用线程组
			// group = AsynchronousChannelGroup.withThreadPool(service);
			// serverChannel = AsynchronousServerSocketChannel.open(group);
			// 开启服务端通道,通过静态方法创建
			serverChannel = AsynchronousServerSocketChannel.open();
			// 绑定监听端口,服务启动,但是还未接收请求
			serverChannel.bind(new InetSocketAddress(port));
			// 开始监听,AIO中,监听是一个类似递归监听操作,每次监听到客户端请求后,都需要处理逻辑开启下一次监听
			// 下一次监听,需要服务器的资源继续支持
			serverChannel.accept(this, new AIOServerHandler());
			try {
				TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ExecutorService getPool() {
		return pool;
	}

	public static void setPool(ExecutorService pool) {
		AioServer.pool = pool;
	}

	public AsynchronousServerSocketChannel getServerChannel() {
		return serverChannel;
	}

	public void setServerChannel(AsynchronousServerSocketChannel serverChannel) {
		this.serverChannel = serverChannel;
	}
}