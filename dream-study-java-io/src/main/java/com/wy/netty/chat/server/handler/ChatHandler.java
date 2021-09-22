package com.wy.netty.chat.server.handler;

import com.wy.netty.chat.ChatCmd;
import com.wy.netty.chat.ModuleId;
import com.wy.netty.chat.annotion.SocketCommand;
import com.wy.netty.chat.annotion.SocketModule;
import com.wy.netty.chat.model.Result;
import com.wy.netty.chat.request.PrivateChatRequest;
import com.wy.netty.chat.request.PublicChatRequest;

/**
 * 聊天
 */
@SocketModule(module = ModuleId.CHAT)
public interface ChatHandler {

	/**
	 * 广播消息
	 * 
	 * @param playerId
	 * @param data {@link PublicChatRequest}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PUBLIC_CHAT)
	Result<?> publicChat(long playerId, byte[] data);

	/**
	 * 私人消息
	 * 
	 * @param playerId
	 * @param data {@link PrivateChatRequest}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PRIVATE_CHAT)
	Result<?> privateChat(long playerId, byte[] data);
}