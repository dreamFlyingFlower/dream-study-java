package com.wy.netty.chat.server.handler;

import com.wy.netty.chat.ModuleId;
import com.wy.netty.chat.PlayerCmd;
import com.wy.netty.chat.annotion.SocketCommand;
import com.wy.netty.chat.annotion.SocketModule;
import com.wy.netty.chat.model.Result;
import com.wy.netty.chat.request.LoginRequest;
import com.wy.netty.chat.request.RegisterRequest;
import com.wy.netty.chat.response.PlayerResponse;
import com.wy.netty.chat.session.Session;

/**
 * 玩家模块
 */
@SocketModule(module = ModuleId.PLAYER)
public interface PlayerHandler {

	/**
	 * 创建并登录帐号
	 * 
	 * @param channel
	 * @param data {@link RegisterRequest}
	 * @return
	 */
	@SocketCommand(cmd = PlayerCmd.REGISTER_AND_LOGIN)
	public Result<PlayerResponse> registerAndLogin(Session session, byte[] data);

	/**
	 * 登录帐号
	 * 
	 * @param channel
	 * @param data {@link LoginRequest}
	 * @return
	 */
	@SocketCommand(cmd = PlayerCmd.LOGIN)
	public Result<PlayerResponse> login(Session session, byte[] data);
}