package com.wy.redis.access;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dream.starter.core.helper.RedisHelper;
import com.wy.common.Constant;
import com.wy.limit.LimitAccessHandler;
import com.wy.limit.annotation.LimitAccess;
import com.wy.redis.idempotent.TokenService;

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
	private RedisHelper redisHelper;

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean handler(LimitAccess limitAccess) {
		ServletRequestAttributes servletRequestAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		Object tokenVal = tokenService.getToken(request, Constant.Redis.TOKEN_LOGIN);
		if (Integer.parseInt(tokenVal.toString()) > limitAccess.count()) {
			log.error("访问频繁,请稍后重试");
			return false;
		}
		int count = 0;
		if (Objects.isNull(tokenVal)) {
			count++;
		}
		if (Integer.parseInt(tokenVal.toString()) <= limitAccess.count()) {
			count = Integer.parseInt(tokenVal.toString());
			count++;
		}
		redisHelper.set(Constant.Redis.TOKEN_LOGIN, count);
		return true;
	}
}