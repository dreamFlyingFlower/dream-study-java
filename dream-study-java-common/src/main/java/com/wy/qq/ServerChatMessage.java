package com.wy.qq;

/**
 * 服务器私聊消息
 */
public class ServerChatMessage extends Message {

	private byte[] recvAddrBytes;

	private byte[] senderAddrBytes;

	private byte[] rawMessageBytes;

	public ServerChatMessage(byte[] recvAddrBytes, byte[] senderAddrBytes, byte[] rawMessageBytes) {
		super();
		this.recvAddrBytes = recvAddrBytes;
		this.senderAddrBytes = senderAddrBytes;
		this.rawMessageBytes = rawMessageBytes;
	}

	public byte[] getRecvAddrBytes() {
		return recvAddrBytes;
	}

	public void setRecvAddrBytes(byte[] recvAddrBytes) {
		this.recvAddrBytes = recvAddrBytes;
	}

	public byte[] getSenderAddrBytes() {
		return senderAddrBytes;
	}

	public void setSenderAddrBytes(byte[] senderAddrBytes) {
		this.senderAddrBytes = senderAddrBytes;
	}

	public byte[] getRawMessageBytes() {
		return rawMessageBytes;
	}

	public void setRawMessageBytes(byte[] rawMessageBytes) {
		this.rawMessageBytes = rawMessageBytes;
	}

	@Override
	public int getMessageType() {
		return SERVER_TO_CLIENT_CHAT;
	}
}
