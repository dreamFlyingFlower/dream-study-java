package com.wy.netty.chat;

/**
 * 聊天类型定义
 */
public interface ChatType {

	/**
	 * 广播消息
	 */
	byte PUBLIC_CHAT = 0;

	/**
	 * 私聊消息
	 */
	byte PRIVATE_CHAT = 1;
}