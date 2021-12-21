package com.wy.aop;

import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.common.util.concurrent.RateLimiter;
import com.wy.enums.TipEnum;
import com.wy.limit.annotation.LimitRate;
import com.wy.result.Result;

/**
 * 限流切面
 *
 * @author 飞花梦影
 * @date 2021-12-21 09:44:28
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Aspect
public class MyRateLimiterAspect {

	private ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

	@Around(value = "@annotation(com.wy.limit.annotation.LimitRate)")
	public Object around(ProceedingJoinPoint joinPoint) {
		try {
			// 获取拦截的方法名
			Signature sig = joinPoint.getSignature();
			// 获取拦截的方法名
			MethodSignature methodSignature = (MethodSignature) sig;
			LimitRate mayiktCurrentLimit = methodSignature.getMethod().getDeclaredAnnotation(LimitRate.class);
			String value = mayiktCurrentLimit.value();
			double limit = mayiktCurrentLimit.limit();
			RateLimiter rateLimiter = rateLimiters.get(value);
			if (rateLimiter == null) {
				rateLimiter = RateLimiter.create(limit);
				rateLimiters.put(value, rateLimiter);
			}
			boolean lock = rateLimiter.tryAcquire();
			if (!lock) {
				return Result.error("当前访问人数过多,请稍后重试!");
			}
			return joinPoint.proceed();
		} catch (Throwable throwable) {
			return Result.error(TipEnum.TIP_SYS_ERROR);
		}
	}
}