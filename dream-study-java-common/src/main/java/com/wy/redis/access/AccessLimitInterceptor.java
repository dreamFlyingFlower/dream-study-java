package com.wy.redis.access;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wy.common.Constant;
import com.wy.limit.annotation.LimitAccess;
import com.wy.redis.RedisUtils;
import com.wy.redis.idempotent.TokenService;

/**
 * Redis接口限流
 * 
 * FIXME 未完成
 *
 * @author 飞花梦影
 * @date 2021-11-09 17:10:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private RedisUtils redisUtils;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		LimitAccess annotation = method.getAnnotation(LimitAccess.class);
		if (Objects.isNull(annotation)) {
			return true;
		}
		Object tokenVal = tokenService.getToken(request, Constant.Redis.TOKEN_LOGIN);
		int count = 0;
		if (Objects.isNull(tokenVal)) {
			count++;
		}
		if (Integer.parseInt(tokenVal.toString()) <= annotation.count()) {
			count = Integer.parseInt(tokenVal.toString());
			count++;
		}
		redisUtils.set(Constant.Redis.TOKEN_LOGIN, count);
		return true;
	}
}