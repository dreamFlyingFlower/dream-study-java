package com.wy.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.common.util.concurrent.RateLimiter;
import com.wy.lang.StrTool;
import com.wy.limit.annotation.LimitRate;
import com.wy.result.ResultException;

/**
 * 限流拦截器,也可以使用AOP.需要加入到拦截器中
 *
 * @author 飞花梦影
 * @date 2021-12-21 09:20:07
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RateLimitInterceptor implements HandlerInterceptor {

	/** 令牌缓存 */
	private ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

	/**
	 * 前置方法:在进入方法之前进行拦截,拿不到参数的具体值
	 * 
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param handler 方法,根据情况,通常情况下可以强转为{@link HandlerMethod}
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			LimitRate limitRate = handlerMethod.getMethod().getAnnotation(LimitRate.class);
			if (limitRate == null) {
				return true;
			}
			String value = limitRate.value();
			if (StrTool.isBlank(value)) {
				value = request.getRequestURI();
			}
			RateLimiter rateLimiter = rateLimiters.get(value);
			if (rateLimiter == null) {
				rateLimiter = RateLimiter.create(limitRate.limit());
				rateLimiters.put(value, rateLimiter);
			}
			// 开始限流
			boolean result = rateLimiter.tryAcquire();
			if (!result) {
				throw new ResultException("当前访问人数过多,请稍后再试");
			}
		}
		return true;
	}
}