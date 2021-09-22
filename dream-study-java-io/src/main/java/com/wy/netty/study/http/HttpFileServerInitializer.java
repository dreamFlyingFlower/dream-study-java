package com.wy.netty.study.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// Uncomment the following line if you want HTTPS
		// SSLEngine engine =
		// SecureChatSslContextFactory.getServerContext().createSSLEngine();
		// engine.setUseClientMode(false);
		// pipeline.addLast("ssl", new SslHandler(engine));
		/**
		 * 1.ReadTimeoutHandler:控制读取数据的时候的超时,10表示如果10秒钟都没有数据读取,那么就引发超时,并关闭当前的channel
		 * 2.WriteTimeoutHandler:控制数据输出的时候的超时,构造参数1表示如果持续1秒钟都没有数据写了,那么就超时
		 * 3.HttpRequestrianDecoder:从读取的数据中将http报文信息解析出来,无非就是什么requestline,header,body什么的...
		 * 4.HttpObjectAggregator:将上面解析出来的http报文的数据组装成为封装好的httprequest对象
		 * 5.HttpresponseEncoder:将用户返回的httpresponse编码成为http报文格式的数据
		 * 6.HttpHandler:自定义的handler,用于处理接收到的http请求
		 */
		// http-request解码器,http服务器端对request解码
		pipeline.addLast("decoder", new HttpRequestDecoder());
		// 对传输文件大少进行限制,单位byte字节
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		// http-response解码器,http服务器端对response编码
		pipeline.addLast("encoder", new HttpResponseEncoder());
		// 向客户端发送数据的一个Handler
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		// 如果是ssl连接就指定为false
		pipeline.addLast("handler", new HttpFileServerHandler(true));
	}
}