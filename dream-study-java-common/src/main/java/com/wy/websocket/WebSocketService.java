package com.wy.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * websocket业务
 *
 * {@link ServerEndpoint}:将当前类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址
 * 
 * @author 飞花梦影
 * @date 2021-01-12 11:10:15
 * @git {@link https://github.com/mygodness100}
 */
@ServerEndpoint("api/websocket")
@Service
@Slf4j
public class WebSocketService {

	// 静态变量,用来记录当前在线连接数
	private static AtomicInteger onlineCount = new AtomicInteger(0);

	// concurrent包的线程安全Set,用来存放每个客户端对应的MyWebSocket对象
	private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<>();

	// 与某个客户端的连接会话,需要通过它来给客户端发送数据
	private Session session;

	// 接收sid
	private String sid = "";

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("sid") String sid) {
		this.session = session;
		webSocketSet.add(this); // 加入set中
		this.sid = sid;
		addOnlineCount(); // 在线数加1
		try {
			sendMessage("conn_success");
			log.info("有新窗口开始监听:" + sid + ",当前在线人数为:" + getOnlineCount());
		} catch (IOException e) {
			log.error("websocket IO Exception");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		// 断开连接情况下,更新主板占用情况为释放
		log.info("释放的sid为:" + sid);
		// 这里写你 释放的时候,要处理的业务
		log.info("有一连接关闭!当前在线人数为" + getOnlineCount());

	}

	/**
	 * 收到客户端消息后调用的方法 @ Param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自窗口" + sid + "的信息:" + message);
		// 群发消息
		for (WebSocketService item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @ Param session @ Param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误");
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 */
	public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
		log.info("推送消息到窗口" + sid + "，推送内容:" + message);

		for (WebSocketService item : webSocketSet) {
			try {
				// 这里可以设定只推送给这个sid的,为null则全部推送
				if (sid == null) {
					// item.sendMessage(message);
				} else if (item.sid.equals(sid)) {
					item.sendMessage(message);
				}
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount.get();
	}

	public static synchronized void addOnlineCount() {
		WebSocketService.onlineCount.getAndIncrement();
	}

	public static synchronized void subOnlineCount() {
		WebSocketService.onlineCount.getAndDecrement();
	}

	public static CopyOnWriteArraySet<WebSocketService> getWebSocketSet() {
		return webSocketSet;
	}
}