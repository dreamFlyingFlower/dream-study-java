package com.wy.netty.chat.session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.wy.netty.chat.model.Response;
import com.wy.netty.chat.serial.Serializer;

import io.netty.buffer.ByteBuf;

/**
 * 会话管理者
 */
public class SessionManager {

	/**
	 * 在线会话
	 */
	private static final ConcurrentHashMap<Long, Session> onlineSessions = new ConcurrentHashMap<>();

	/**
	 * 加入
	 * 
	 * @param playerId
	 * @param channel
	 * @return
	 */
	public static boolean putSession(long playerId, Session session) {
		if (!onlineSessions.containsKey(playerId)) {
			boolean success = onlineSessions.putIfAbsent(playerId, session) == null ? true : false;
			return success;
		}
		return false;
	}

	/**
	 * 移除
	 * 
	 * @param playerId
	 */
	public static Session removeSession(long playerId) {
		return onlineSessions.remove(playerId);
	}

	/**
	 * 发送消息[自定义协议]
	 * 
	 * @param <T>
	 * @param playerId
	 * @param message
	 */
	public static <T extends Serializer> void sendMessage(long playerId, short module, short cmd, T message) {
		Session session = onlineSessions.get(playerId);
		if (session != null && session.isConnected()) {
			Response response = new Response(module, cmd, message.getBytes());
			session.write(response);
		}
	}

	/**
	 * 发送消息[protoBuf协议]
	 * 
	 * @param <T>
	 * @param playerId
	 * @param message
	 */
	public static <T extends ByteBuf> void sendMessage(long playerId, short module, short cmd, T message) {
		Session session = onlineSessions.get(playerId);
		if (session != null && session.isConnected()) {
			Response response = new Response(module, cmd, message.array());
			session.write(response);
		}
	}

	/**
	 * 是否在线
	 * 
	 * @param playerId
	 * @return
	 */
	public static boolean isOnlinePlayer(long playerId) {
		return onlineSessions.containsKey(playerId);
	}

	/**
	 * 获取所有在线玩家
	 * 
	 * @return
	 */
	public static Set<Long> getOnlinePlayers() {
		return Collections.unmodifiableSet(onlineSessions.keySet());
	}
}