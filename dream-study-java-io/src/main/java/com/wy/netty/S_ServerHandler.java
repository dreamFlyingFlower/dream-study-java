package com.wy.netty;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * netty服务端启动逻辑
 * 
 * Sharable:代表该Handler是一个可分享的处理器,服务器注册此Handler后,可分享给多个客户端同时使用
 * 如果不使用注解,则每次客户端请求时,必须为客户端重新创建一个新的Handler对象<br>
 * 如果handler是一个Sharable的,一定避免定义可写的实例变量
 * 
 * @author ParadiseWy
 * @date 2019年5月14日 下午9:11:34
 * @git {@link https://github.com/mygodness100}
 */
@Sharable
public class S_ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	/**
	 * 业务处理逻辑 用于处理读取数据请求的逻辑
	 * 
	 * @param ctx 上下文对象.其中包含于客户端建立连接的所有资源, 如:对应的Channel
	 * @param msg 读取到的数据. 默认类型是ByteBuf,Netty自带,是对ByteBuffer的封装,不需要考虑复位问题(flip)
	 */
	// @Override
	public void childHandler(ChannelHandlerContext ctx, Object msg) {
		// 获取读取的数据, 是一个缓冲
		ByteBuf readBuffer = (ByteBuf) msg;
		// 创建一个字节数组,用于保存缓存中的数据
		byte[] tempDatas = new byte[readBuffer.readableBytes()];
		// 将缓存中的数据读取到字节数组中
		readBuffer.readBytes(tempDatas);
		String message = new String(tempDatas, StandardCharsets.UTF_8);
		System.out.println("from client : " + message);
		if ("exit".equals(message)) {
			ctx.close();
			return;
		}
		String line = "server message to client!";
		// 写操作自动释放缓存,避免内存溢出问题
		try {
			ctx.writeAndFlush(Unpooled.copiedBuffer(line.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 注意,如果调用的是write(),不会刷新缓存,缓存中的数据不会发送到客户端,必须再次调用flush方法才行
		// ctx.write(Unpooled.copiedBuffer(line.getBytes("UTF-8")));
		// ctx.flush();
	}
}