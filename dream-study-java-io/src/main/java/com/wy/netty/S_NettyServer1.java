package com.wy.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

/**
 * Netty服务端1
 * 
 * @author ParadiseWY
 * @date 2020-11-28 15:42:15
 * @git {@link https://github.com/mygodness100}
 */
public class S_NettyServer1 {

	public static void main(String[] args) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					// 设置channel工厂
					.channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, true)
					.childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue").handler(new S_ServerHandler1())
					// 设置管道工厂
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						public void initChannel(SocketChannel ch) {
							// 如果第一个加入到管道中的是字符串解码,则下一个管道中的handler参数就是String
							// ch.pipeline().addLast(new StringDecoder());
							// 配置多个handler,对每一个新连接的处理逻辑
							ch.pipeline().addLast(new S_AuthHandler());
						}
					});
			// 端口绑定,同步启动
			ChannelFuture f = b.bind(8888).sync();
			// 关闭
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}