package com.wy.netty.timer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.wy.model.NtClient;
import com.wy.util.MarshallingUtils;
import com.wy.util.NettyUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * 定时断线重连
 * 
 * 客户端数据多,需要传递的数据量级较大,可周期性的发送数据,需要对数据的及时性要求不高才可使用
 * 
 * @author 飞花梦影
 * @date 2021-09-02 23:27:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Client {

	private EventLoopGroup group = null;

	private Bootstrap bootstrap = null;

	private ChannelFuture future = null;

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
		// bootstrap.handler(new LoggingHandler(LogLevel.INFO));
	}

	public void setHandlers() throws InterruptedException {
		this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingDecoder());
				ch.pipeline().addLast(MarshallingUtils.buildMarshallingEncoder());
				// 写操作自动断线,在指定时间内,没有写操作,自动断线,默认单位是秒
				ch.pipeline().addLast(new WriteTimeoutHandler(3));
				ch.pipeline().addLast(new S_ClientHandler());
			}
		});
	}

	/**
	 * 断线重连
	 * 
	 * @param host ip地址
	 * @param port 端口
	 * @return 连接
	 */
	public ChannelFuture getChannelFuture(String host, int port) {
		try {
			// future是否被回收
			if (future == null) {
				future = this.bootstrap.connect(host, port).sync();
			}
			// future中的连接通道是否有效
			if (!future.channel().isActive()) {
				future = this.bootstrap.connect(host, port).sync();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			client.setHandlers();
			future = client.getChannelFuture("localhost", 9999);
			for (int i = 0; i < 3; i++) {
				NtClient msg = new NtClient(new Random().nextLong(), "test" + i, new byte[0]);
				future.channel().writeAndFlush(msg);
				TimeUnit.SECONDS.sleep(2);
			}
			TimeUnit.SECONDS.sleep(5);
			future = client.getChannelFuture("localhost", 9999);
			NtClient msg = new NtClient(new Random().nextLong(), "test", new byte[0]);
			future.channel().writeAndFlush(msg);
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