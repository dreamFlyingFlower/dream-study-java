package com.wy.netty.chat.server.service;

import com.wy.netty.chat.response.PlayerResponse;
import com.wy.netty.chat.session.Session;

/**
 * 玩家服务
 */
public interface PlayerService {

	/**
	 * 登录注册用户
	 * 
	 * @param playerName
	 * @param passward
	 * @return
	 */
	PlayerResponse registerAndLogin(Session session, String playerName, String passward);

	/**
	 * 登录
	 * 
	 * @param playerName
	 * @param passward
	 * @return
	 */
	PlayerResponse login(Session session, String playerName, String passward);
}