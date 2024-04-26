package com.wy.netty.chat.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.dream.collection.CollectionHelper;
import com.wy.netty.chat.exception.ErrorCodeException;
import com.wy.netty.chat.model.ResultCode;
import com.wy.netty.chat.response.PlayerResponse;
import com.wy.netty.chat.server.entity.Player;
import com.wy.netty.chat.server.repository.PlayerRepository;
import com.wy.netty.chat.session.Session;
import com.wy.netty.chat.session.SessionManager;

/**
 * 玩家服务
 */
@Component
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	@Override
	public PlayerResponse registerAndLogin(Session session, String playerName, String passward) {
		List<Player> existPlayers =
				playerRepository.findAll(Example.of(Player.builder().playerName(playerName).build()));
		// 玩家名已被占用
		if (CollectionHelper.isNotEmpty(existPlayers)) {
			throw new ErrorCodeException(ResultCode.PLAYER_EXIST);
		}

		// 创建新帐号
		Player player = new Player();
		player.setPlayerName(playerName);
		player.setPassward(passward);
		player = playerRepository.saveAndFlush(player);

		// 顺便登录
		return login(session, playerName, passward);
	}

	@Override
	public PlayerResponse login(Session session, String playerName, String passward) {

		// 判断当前会话是否已登录
		if (session.getAttachment() != null) {
			throw new ErrorCodeException(ResultCode.HAS_LOGIN);
		}
		List<Player> existPlayers =
				playerRepository.findAll(Example.of(Player.builder().playerName(playerName).build()));
		// 玩家不存在
		if (CollectionHelper.isEmpty(existPlayers)) {
			throw new ErrorCodeException(ResultCode.PLAYER_NO_EXIST);
		}

		Player player = existPlayers.get(0);
		// 密码错误
		if (!player.getPassward().equals(passward)) {
			throw new ErrorCodeException(ResultCode.PASSWARD_ERROR);
		}

		// 判断是否在其他地方登录过
		boolean onlinePlayer = SessionManager.isOnlinePlayer(player.getPlayerId());
		if (onlinePlayer) {
			Session oldSession = SessionManager.removeSession(player.getPlayerId());
			oldSession.removeAttachment();
			// 踢下线
			oldSession.close();
		}

		// 加入在线玩家会话
		if (SessionManager.putSession(player.getPlayerId(), session)) {
			session.setAttachment(player);
		} else {
			throw new ErrorCodeException(ResultCode.LOGIN_FAIL);
		}

		// 创建Response传输对象返回
		PlayerResponse playerResponse = new PlayerResponse();
		playerResponse.setPlayerId(player.getPlayerId());
		playerResponse.setPlayerName(player.getPlayerName());
		playerResponse.setLevel(player.getLevel());
		playerResponse.setExp(player.getExp());
		return playerResponse;
	}
}