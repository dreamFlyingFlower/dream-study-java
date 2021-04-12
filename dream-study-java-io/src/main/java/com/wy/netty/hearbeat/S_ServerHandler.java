package com.wy.netty.hearbeat;

import java.util.ArrayList;
import java.util.List;

import com.wy.model.Heatbeat;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class S_ServerHandler extends ChannelInboundHandlerAdapter {

	private static List<String> credentials = new ArrayList<>();
	private static final String HEATBEAT_SUCCESS = "SERVER_RETURN_HEATBEAT_SUCCESS";

	public S_ServerHandler() {
		// 初始化客户端列表信息。一般通过配置文件读取或数据库读取。
		credentials.add("192.168.199.222_WIN-QIUB2JF5TDP");
	}

	// 业务处理逻辑
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof String) {
			this.checkCredential(ctx, msg.toString());
		} else if (msg instanceof Heatbeat) {
			this.readHeatbeatMessage(ctx, msg);
		} else {
			ctx.writeAndFlush("wrong message").addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void readHeatbeatMessage(ChannelHandlerContext ctx, Object msg) {
		Heatbeat message = (Heatbeat) msg;
		System.out.println(message);
		System.out.println("=======================================");
		ctx.writeAndFlush("receive heatbeat message");
	}

	/**
	 * 身份检查。检查客户端身份是否有效。 客户端身份信息应该是通过数据库或数据文件定制的。 身份通过 - 返回确认消息。 身份无效 - 断开连接
	 * @param ctx
	 * @param credential
	 */
	private void checkCredential(ChannelHandlerContext ctx, String credential) {
		System.out.println(credential);
		System.out.println(credentials);
		if (credentials.contains(credential)) {
			ctx.writeAndFlush(HEATBEAT_SUCCESS);
		} else {
			ctx.writeAndFlush("no credential contains").addListener(ChannelFutureListener.CLOSE);
		}
	}

	// 异常处理逻辑
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("server exceptionCaught method run...");
		// cause.printStackTrace();
		ctx.close();
	}

}
