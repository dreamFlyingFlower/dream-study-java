package com.wy.netty.chat.client.handler;

import com.wy.netty.chat.ModuleId;
import com.wy.netty.chat.PlayerCmd;
import com.wy.netty.chat.annotion.SocketCommand;
import com.wy.netty.chat.annotion.SocketModule;

/**
 * 玩家模块
 */
@SocketModule(module = ModuleId.PLAYER)
public interface PlayerHandler {

	/**
	 * 创建并登录帐号
	 * 
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = PlayerCmd.REGISTER_AND_LOGIN)
	public void registerAndLogin(int resultCode, byte[] data);

	/**
	 * 创建并登录帐号
	 * 
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = PlayerCmd.LOGIN)
	public void login(int resultCode, byte[] data);
}