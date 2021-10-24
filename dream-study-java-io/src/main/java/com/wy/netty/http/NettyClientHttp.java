package com.wy.netty.http;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.DefaultPromise;

/**
 * 模拟Http请求体body获取异常的客户端
 * 
 * @author 飞花梦影
 * @date 2021-10-23 16:43:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyClientHttp {

	private Channel channel;

	private NettyClientHttpHandler handler = new NettyClientHttpHandler();

	public static void main(String[] args) {
		NettyClientHttp nettyClientHttp = new NettyClientHttp();
		nettyClientHttp.connect("127.0.0.1", 9999);
		ByteBuf byteBuf = Unpooled.wrappedBuffer("this is a test".getBytes(StandardCharsets.UTF_8));
		DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
				"http://127.0.0.1:9999/user?id=10&username=test", byteBuf);
		NettyHttpResponse response = nettyClientHttp.sendGet(request);
		System.out.println(new String(response.body()));
	}

	private void connect(String uri, int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new HttpClientCodec());
				ch.pipeline().addLast(new HttpObjectAggregator(Short.MAX_VALUE));
				ch.pipeline().addLast(handler);
			}
		});
		ChannelFuture future;
		try {
			future = bootstrap.connect(uri, port).sync();
			channel = future.channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private NettyHttpResponse sendGet(FullHttpRequest request) {
		request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
		DefaultPromise<NettyHttpResponse> promise = new DefaultPromise<>(channel.eventLoop());
		handler.setPromise(promise);
		channel.writeAndFlush(request);
		NettyHttpResponse httpResponse = null;
		try {
			// 线程阻塞,调用NettyClientHttpHandler中的channelRead0方法
			httpResponse = promise.get();
			if (Objects.nonNull(httpResponse)) {
				// 此处会报错
				System.out.println(httpResponse.body());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return httpResponse;
	}
}