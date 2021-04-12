package com.wy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 利用Netty建立一个客户端
 * 
 * @author ParadiseWy
 * @date 2019年5月14日 下午8:59:50
 * @git {@link https://github.com/mygodness100}
 */
public class S_ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf readBuffer = (ByteBuf) msg;
			byte[] tempDatas = new byte[readBuffer.readableBytes()];
			readBuffer.readBytes(tempDatas);
			System.out.println("from server : " + new String(tempDatas, "UTF-8"));
		} finally {
			// 用于释放缓存,避免内存溢出
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("client exceptionCaught method run...");
		ctx.close();
	}

	/*
	 * @Override // 断开连接时执行 public void channelInactive(ChannelHandlerContext ctx)
	 * throws Exception { System.out.println("channelInactive method run..."); }
	 * 
	 * @Override // 连接通道建立成功时执行 public void channelActive(ChannelHandlerContext ctx)
	 * throws Exception { System.out.println("channelActive method run..."); }
	 * 
	 * @Override // 每次读取完成时执行 public void channelReadComplete(ChannelHandlerContext
	 * ctx) throws Exception {
	 * System.out.println("channelReadComplete method run..."); }
	 */
}