package com.wy.qq;

/**
 * 消息抽象类
 */
public abstract class Message {

	/** 服务器发送给客户端的消息类型:刷新列表 */
	public static final int SERVER_TO_CLIENT_REFRESHFRIENDLIST = 1;

	/** 服务器发送给客户端的消息类型:群聊 */
	public static final int SERVER_TO_CLIENT_CHATS = 2;

	/** 服务器发送给客户端的消息类型:私聊 */
	public static final int SERVER_TO_CLIENT_CHAT = 3;

	/** 客户端发送给服务器的消息类型 */
	public static final int CLIENT_TO_SERVER_REFRESHFRIENDLIST = 4;

	public static final int CLIENT_TO_SERVER_CHATS = 5;

	public static final int CLIENT_TO_SERVER_CHAT = 6;

	public abstract int getMessageType();
}