package com.wy.netty.chat.client;

import com.wy.netty.chat.client.swing.Swingclient;
import com.wy.netty.chat.model.Response;
import com.wy.netty.chat.scanner.Invoker;
import com.wy.netty.chat.scanner.InvokerHoler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息接受处理类
 */
public class ClientHandler extends SimpleChannelInboundHandler<Response> {

	/**
	 * 界面
	 */
	private Swingclient swingclient;

	public ClientHandler(Swingclient swingclient) {
		this.swingclient = swingclient;
	}

	/**
	 * 接收消息
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
		handlerResponse(response);
	}

	/**
	 * 消息处理
	 * 
	 * @param channelId
	 * @param request
	 */
	private void handlerResponse(Response response) {
		// 获取命令执行器
		Invoker invoker = InvokerHoler.getInvoker(response.getModule(), response.getCmd());
		if (invoker != null) {
			try {
				invoker.invoke(response.getStateCode(), response.getData());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 找不到执行器
			System.out.println(String.format("module:%s  cmd:%s 找不到命令执行器", response.getModule(), response.getCmd()));
		}
	}

	/**
	 * 断开链接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		swingclient.getTips().setText("与服务器断开连接~~~");
	}
}