package com.wy.redis.idempotent;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wy.common.Constant;
import com.wy.idempotent.Idempotency;
import com.wy.result.ResultException;

/**
 * 幂等接口拦截,需要使用{@link Idempotency},同时配置Web拦截器
 *
 * @author 飞花梦影
 * @date 2021-11-09 15:48:07
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class IdempotentInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Idempotency annotation = method.getAnnotation(Idempotency.class);
		if (annotation != null) {
			if (!tokenService.checkToken(request, Constant.Redis.TOKEN_IDEMPOTENT)) {
				throw new ResultException("请不要重复提交");
			}
		}
		return true;
	}
}