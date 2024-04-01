package com.wy.redis.access;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dream.lang.StrHelper;
import com.dream.limit.LimitAccessHandler;
import com.dream.limit.annotation.LimitAccess;
import com.dream.result.ResultException;
import com.wy.common.Constant;
import com.wy.redis.idempotent.TokenService;

import dream.flying.flower.autoconfigure.web.helper.RedisHelpers;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用token进行访问次数限制
 *
 * @author 飞花梦影
 * @date 2022-06-23 09:43:09
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class TokenAccessLimitHandler implements LimitAccessHandler {

	@Autowired
	private RedisHelpers redisHelper;

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean handler(LimitAccess limitAccess) {
		ServletRequestAttributes servletRequestAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		String token = tokenService.getToken(request, Constant.Redis.TOKEN_LOGIN);
		if (StrHelper.isBlank(token)) {
			throw new ResultException("token不存在,请检查!");
		}

		int loginNum = 0;
		Object count = redisHelper.get(token);
		if (Objects.isNull(count)) {
			loginNum++;
		} else {
			loginNum = Integer.parseInt(count.toString());
		}

		if (loginNum > limitAccess.count()) {
			log.error("访问频繁,请稍后重试");
			return false;
		}
		if (loginNum <= limitAccess.count()) {
			loginNum++;
		}
		redisHelper.set(token, loginNum);
		return true;
	}
}