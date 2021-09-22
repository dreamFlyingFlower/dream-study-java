package com.wy.netty.study.hearbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

/**
 * 利用Netty自带的{@link IdleStateHandler}完成心跳检测
 *
 * @author 飞花梦影
 * @date 2021-09-18 14:05:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_Server01 {

	public static void main(String[] args) {
		// 服务类
		ServerBootstrap bootstrap = new ServerBootstrap();
		// boss和worker
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		try {
			// 设置线程池
			bootstrap.group(boss, worker);
			// 设置socket工厂
			bootstrap.channel(NioServerSocketChannel.class);
			// 设置管道工厂
			bootstrap.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					// 心跳检测专用类:超过多长时间没有读操作,超过多长时间没有写操作,超过多长时间没有读写操作,单位都是s
					ch.pipeline().addLast(new IdleStateHandler(5, 5, 10));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new StringEncoder());
					ch.pipeline().addLast(new S_ServerHandler01());
				}
			});

			// 设置serverSocketchannel的设置,链接缓冲池的大小
			bootstrap.option(ChannelOption.SO_BACKLOG, 2048);
			// socketchannel的设置,维持链接的活跃,清除死链接
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			// socketchannel的设置,关闭延迟发送
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			// 绑定端口
			ChannelFuture future = bootstrap.bind(10101);
			System.out.println("start......");
			// 等待链接关闭
			future.channel().closeFuture().sync();
			// 获取结果
			future.channel().attr(AttributeKey.valueOf("key")).get();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}