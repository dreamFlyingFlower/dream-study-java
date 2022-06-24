package com.wy.redis.access;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wy.limit.annotation.LimitAccess;

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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		if (!method.isAnnotationPresent(LimitAccess.class)) {
			return true;
		}
		LimitAccess limitAccess = method.getAnnotation(LimitAccess.class);
		if (Objects.isNull(limitAccess)) {
			return true;
		}
		if (limitAccess.custom()) {
			return limitAccess.handler().newInstance().handler(limitAccess);
		} else {
			return new DefaultAccessLimitHandler().handler(limitAccess);
		}
	}
}