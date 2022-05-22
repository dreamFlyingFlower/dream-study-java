package com.wy.qq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 服务器群聊消息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ServerChatsMessage extends Message {

	private String senderAddr;

	private byte[] rawMessage;

	@Override
	public int getMessageType() {
		return SERVER_TO_CLIENT_CHATS;
	}
}