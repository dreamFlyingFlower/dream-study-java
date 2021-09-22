package com.wy.netty.chat.server.service;

/**
 * 聊天服务
 */
public interface ChatService {

	/**
	 * 群发消息
	 * 
	 * @param playerId
	 * @param content
	 */
	void publicChat(long playerId, String content);

	/**
	 * 私聊
	 * 
	 * @param playerId
	 * @param targetPlayerId
	 * @param content
	 */
	void privateChat(long playerId, long targetPlayerId, String content);
}