package com.wy.netty.chat.request;

import com.wy.netty.chat.serial.Serializer;

/**
 * 广播消息
 */
public class PublicChatRequest extends Serializer {

	/**
	 * 内容
	 */
	private String context;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	protected void read() {
		this.context = readString();
	}

	@Override
	protected void write() {
		writeString(context);
	}
}