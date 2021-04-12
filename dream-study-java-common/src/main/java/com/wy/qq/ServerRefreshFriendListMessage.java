package com.wy.qq;

/**
 * 服务器刷新好友列表
 */
public class ServerRefreshFriendListMessage extends Message {

	public int getMessageType() {
		return SERVER_TO_CLIENT_REFRESHFRIENDLIST;
	}
}