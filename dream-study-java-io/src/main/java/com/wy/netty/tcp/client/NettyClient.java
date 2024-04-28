package com.wy.netty.tcp.client;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dream.framework.web.helper.SpringContextHelpers;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty客户端为多实例,每个实例绑定一个线程,持续阻塞到客户端关闭为止,每个客户端中可以保存自己的业务数据,以便在后续与服务端交互时处理业务使用.
 * 客户端执行连接时,给了2次重试的机会,如果3次都没连接成功则放弃.后续可以选择将该消息重新入列消费.
 * 
 * 在实际项目中,此处还应该预先给服务端发送一条登录消息,待服务端确认后才能执行后续通讯,这需要视实际情况进行调整
 * 需要注意的点是EventLoopGroup是从构造函数传入的,而不是在客户端中创建的,因为当客户端数量非常多时,每个客户端都创建自己的线程组会极大的消耗服务器资源,
 * 因此在实际使用中是按业务去创建统一的线程组给该业务下的所有客户端共同使用的,线程组的大小需要根据业务需求灵活配置
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:16:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
@Scope("prototype")
@Getter
@NoArgsConstructor
public class NettyClient implements Runnable {

	@Value("${netty.server.port}")
	private int port;

	@Value("${netty.server.host}")
	private String host;

	// 客户端唯一标识
	private String imei;

	// 自定义业务数据
	private Map<String, Object> bizData;

	private EventLoopGroup workGroup;

	private Class<BaseClientHandler> clientHandlerClass;

	private ChannelFuture channelFuture;

	public NettyClient(String imei, Map<String, Object> bizData, EventLoopGroup workGroup,
			Class<BaseClientHandler> clientHandlerClass) {
		this.imei = imei;
		this.bizData = bizData;
		this.workGroup = workGroup;
		this.clientHandlerClass = clientHandlerClass;
	}

	@Override
	public void run() {
		try {
			this.init();
			log.info("客户端启动imei={}", imei);
		} catch (Exception e) {
			log.error("客户端启动失败:{}", e.getMessage(), e);
		}
	}

	public void close() {
		if (null != this.channelFuture) {
			this.channelFuture.channel().close();
		}
		NettyClientHolder.get().remove(this.imei);
	}

	public void send(String message) {
		try {
			if (!this.channelFuture.channel().isActive()) {
				log.info("通道不活跃imei={}", this.imei);
				return;
			}
			if (!StringUtils.isEmpty(message)) {
				log.info("队列消息发送===>{}", message);
				this.channelFuture.channel().writeAndFlush(message);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void init() throws Exception {
		// 将本实例传递到handler
		BaseClientHandler clientHandler = SpringContextHelpers.getBean(clientHandlerClass, this);
		Bootstrap b = new Bootstrap();
		// 2 通过辅助类去构造server/client
		b.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
				.option(ChannelOption.SO_RCVBUF, 1024 * 32).option(ChannelOption.SO_SNDBUF, 1024 * 32)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(
								new DelimiterBasedFrameDecoder(1024 * 1024, Unpooled.copiedBuffer("\r\n".getBytes())));
						ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));// String解码。
						ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));// String解码。
						// // 心跳设置
						ch.pipeline().addLast(new IdleStateHandler(0, 0, 600, TimeUnit.SECONDS));
						ch.pipeline().addLast(clientHandler);
					}
				});
		this.connect(b);
	}

	private void connect(Bootstrap b) throws InterruptedException {
		long c1 = System.currentTimeMillis();
		final int maxRetries = 2; // 重连2次
		final AtomicInteger count = new AtomicInteger();
		final AtomicBoolean flag = new AtomicBoolean(false);
		try {
			this.channelFuture = b.connect(host, port).addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						if (count.incrementAndGet() > maxRetries) {
							log.warn("imei={}重连超过{}次", imei, maxRetries);
						} else {
							log.info("imei={}重连第{}次", imei, count);
							b.connect(host, port).addListener(this);
						}

					} else {
						log.info("imei={}连接成功,连接IP:{}连接端口:{}", imei, host, port);
						flag.set(true);
					}
				}
			}).sync(); // 同步连接
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("设备imei={}，channelId={}连接耗时={}ms", imei, channelFuture.channel().id(),
				System.currentTimeMillis() - c1);
		if (flag.get()) {
			channelFuture.channel().closeFuture().sync(); // 连接成功后将持续阻塞该线程
		}
	}
}