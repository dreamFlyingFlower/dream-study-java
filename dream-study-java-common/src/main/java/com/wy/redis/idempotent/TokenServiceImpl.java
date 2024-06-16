package com.wy.redis.idempotent;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.common.Constant;

import dream.flying.flower.autoconfigure.web.helper.RedisHelpers;
import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;

/**
 * Token业务实现类
 *
 * @author 飞花梦影
 * @date 2021-11-09 16:21:50
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private RedisHelpers redisHelpers;

	@Override
	public String createToken() {
		StringBuilder builder = new StringBuilder();
		builder.append(Constant.Redis.TOKEN_PREFIX).append(DigestHelper.uuid());
		redisHelpers.setNX(builder.toString(), builder.toString(), 10000L);
		return builder.toString();
	}

	@Override
	public boolean checkToken(HttpServletRequest request, String tokenKey) {
		String token = request.getHeader(tokenKey);
		if (StrHelper.isBlank(token)) {
			token = request.getParameter(tokenKey);
			if (StrHelper.isBlank(token)) {
				throw new ResultException(TipEnum.TIP_AUTH_TOKEN_EMPTY);
			}
		}

		if (!redisHelpers.exist(tokenKey)) {
			throw new ResultException(TipEnum.TIP_AUTH_TOKEN_NOT_EXIST);
		}
		boolean success = redisHelpers.atomicCompareAndDelete(tokenKey, token);
		if (!success) {
			throw new ResultException(TipEnum.TIP_AUTH_TOKEN_NOT_EXIST);
		}
		return true;
	}

	@Override
	public String getToken(HttpServletRequest request, String tokenKey) {
		String token = request.getHeader(tokenKey);
		if (StrHelper.isBlank(token)) {
			token = request.getParameter(tokenKey);
			if (StrHelper.isBlank(token)) {
				throw new ResultException(TipEnum.TIP_AUTH_TOKEN_EMPTY);
			}
		}
		return token;
	}
}