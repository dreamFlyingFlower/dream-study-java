package com.wy.websocket.netty;

import java.util.Objects;

import com.alibaba.fastjson2.JSON;
import com.wy.websocket.MyWebSocketMessage;
import com.wy.websocket.WebSocketType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-30 10:29:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class WebSocketServerListenerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// 获取客户端所传输的消息
		String content = msg.text();
		// 获取客户端发来的消息
		MyWebSocketMessage webSocketMessage = JSON.parseObject(content, MyWebSocketMessage.class);
		assert webSocketMessage != null;
		System.out.println("----->" + webSocketMessage);
		String action = webSocketMessage.getType();
		Channel channel = ctx.channel();

		// 判断消息类型,根据不同的类型来处理不同的业务
		if (Objects.equals(action, WebSocketType.OPEN.name())) {
			// 当websocket 第一次open的时候,初始化channel,把用的channel 和 userid 关联起来
			String senderId = webSocketMessage.getFrom();
			UserConnectPool.getChannelMap().put(senderId, channel);
		} else if (Objects.equals(action, WebSocketType.IDLE.name())) {
			// 心跳类型的消息
			System.out.println("收到来自channel 为[" + channel + "]的心跳包");
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// 接收到请求
		log.info("有新的客户端链接：[{}]", ctx.channel().id().asLongText());
		UserConnectPool.getChannelGroup().add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String chanelId = ctx.channel().id().asShortText();
		log.info("客户端被移除：channel id 为：" + chanelId);
		UserConnectPool.getChannelGroup().remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		// 发生了异常后关闭连接,同时从channelgroup移除
		ctx.channel().close();
		UserConnectPool.getChannelGroup().remove(ctx.channel());
	}
}