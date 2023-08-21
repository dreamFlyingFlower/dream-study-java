package com.wy.redis.idempotent;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.common.Constant;
import com.wy.digest.DigestTool;
import com.wy.enums.TipEnum;
import com.wy.lang.StrTool;
import com.wy.result.ResultException;

import dream.flying.flower.autoconfigure.web.helper.RedisHelper;

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
	private RedisHelper redisHelper;

	@Override
	public String createToken() {
		StringBuilder builder = new StringBuilder();
		builder.append(Constant.Redis.TOKEN_PREFIX).append(DigestTool.uuid());
		redisHelper.setNX(builder.toString(), builder.toString(), 10000L);
		return builder.toString();
	}

	@Override
	public boolean checkToken(HttpServletRequest request, String tokenKey) {
		String token = request.getHeader(tokenKey);
		if (StrTool.isBlank(token)) {
			token = request.getParameter(tokenKey);
			if (StrTool.isBlank(token)) {
				throw new ResultException(TipEnum.TIP_AUTH_TOKEN_EMPTY);
			}
		}
		if (!redisHelper.exist(token)) {
			throw new ResultException(TipEnum.TIP_AUTH_TOKEN_NOT_EXIST);
		}
		boolean remove = redisHelper.clear(token);
		if (!remove) {
			throw new ResultException(TipEnum.TIP_AUTH_TOKEN_NOT_EXIST);
		}
		return true;
	}

	@Override
	public Object getToken(HttpServletRequest request, String tokenKey) {
		String token = request.getHeader(tokenKey);
		if (StrTool.isBlank(token)) {
			token = request.getParameter(tokenKey);
			if (StrTool.isBlank(token)) {
				throw new ResultException(TipEnum.TIP_AUTH_TOKEN_EMPTY);
			}
		}
		return token;
	}
}