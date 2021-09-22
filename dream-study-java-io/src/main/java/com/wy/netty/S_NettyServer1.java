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
import io.netty.handler.codec.string.StringEncoder;
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
			// 设置线程池
			b.group(bossGroup, workerGroup)
					// 设置channel工厂
					.channel(NioServerSocketChannel.class)
					// 设置serversocketchannel的最大连接数
					.option(ChannelOption.SO_BACKLOG, 2048)
					// 设置socketchannel的死链接,即socketchannel在2小时左右没有写入操作时,断掉链接
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					// 设置socketchannel,关闭延迟发送
					.childOption(ChannelOption.TCP_NODELAY, true)
					// 设置属性
					.childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue").handler(new S_ServerHandler1())
					// 设置管道工厂
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						public void initChannel(SocketChannel ch) {
							// 如果第一个加入到管道中的是字符串解码,则下一个管道中的handler参数就是String
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new StringEncoder());
							// 配置多个handler,对每一个新连接的处理逻辑.如果没有上面的String解码,则handler接收的是ByteBuf类型
							ch.pipeline().addLast(new S_NettyServer1Handler());
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