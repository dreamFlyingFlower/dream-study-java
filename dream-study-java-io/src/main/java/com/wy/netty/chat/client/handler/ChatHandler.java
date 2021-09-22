package com.wy.netty.chat.client.handler;

import com.wy.netty.chat.ChatCmd;
import com.wy.netty.chat.ModuleId;
import com.wy.netty.chat.annotion.SocketCommand;
import com.wy.netty.chat.annotion.SocketModule;
import com.wy.netty.chat.response.ChatResponse;

/**
 * 聊天
 */
@SocketModule(module = ModuleId.CHAT)
public interface ChatHandler {

	/**
	 * 发送广播消息回包
	 * 
	 * @param resultCode
	 * @param data {@link null}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PUBLIC_CHAT)
	void publicChat(int resultCode, byte[] data);

	/**
	 * 发送私人消息回包
	 * 
	 * @param resultCode
	 * @param data {@link null}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PRIVATE_CHAT)
	void privateChat(int resultCode, byte[] data);

	/**
	 * 收到推送聊天信息
	 * 
	 * @param resultCode
	 * @param data {@link ChatResponse}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PUSHCHAT)
	void receieveMessage(int resultCode, byte[] data);
}