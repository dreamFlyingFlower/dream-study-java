package com.wy.netty.chat.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wy.netty.chat.exception.ErrorCodeException;
import com.wy.netty.chat.model.Result;
import com.wy.netty.chat.model.ResultCode;
import com.wy.netty.chat.request.LoginRequest;
import com.wy.netty.chat.request.RegisterRequest;
import com.wy.netty.chat.response.PlayerResponse;
import com.wy.netty.chat.server.service.PlayerService;
import com.wy.netty.chat.session.Session;

/**
 * 玩家模块
 */
@Component
public class PlayerHandlerImpl implements PlayerHandler {

	@Autowired
	private PlayerService playerService;

	@Override
	public Result<PlayerResponse> registerAndLogin(Session session, byte[] data) {

		PlayerResponse result = null;
		try {
			// 反序列化
			RegisterRequest registerRequest = new RegisterRequest();
			registerRequest.readFromBytes(data);

			// 参数判空
			if (!StringUtils.hasText(registerRequest.getPlayerName())
					|| !StringUtils.hasText(registerRequest.getPassward())) {
				return Result.ERROR(ResultCode.PLAYERNAME_NULL);
			}

			// 执行业务
			result = playerService.registerAndLogin(session, registerRequest.getPlayerName(),
					registerRequest.getPassward());
		} catch (ErrorCodeException e) {
			return Result.ERROR(e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
		}
		return Result.SUCCESS(result);
	}

	@Override
	public Result<PlayerResponse> login(Session session, byte[] data) {
		PlayerResponse result = null;
		try {
			// 反序列化
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.readFromBytes(data);

			// 参数判空
			if (!StringUtils.hasText(loginRequest.getPlayerName())
					|| !StringUtils.hasText(loginRequest.getPassward())) {
				return Result.ERROR(ResultCode.PLAYERNAME_NULL);
			}

			// 执行业务
			result = playerService.login(session, loginRequest.getPlayerName(), loginRequest.getPassward());
		} catch (ErrorCodeException e) {
			return Result.ERROR(e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
		}
		return Result.SUCCESS(result);
	}
}