package com.wy.redis.delete;

import java.lang.reflect.Method;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 延时双删切面
 *
 * @author 飞花梦影
 * @date 2024-01-04 10:04:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Aspect
@Component
public class ClearAndReloadCacheAspect {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Pointcut("@annotation(com.wy.redis.delete.ClearAndReloadCache)")
	public void pointCut() {

	}

	@Around("pointCut()")
	public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
		Signature signature = proceedingJoinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method targetMethod = methodSignature.getMethod();
		ClearAndReloadCache annotation = targetMethod.getAnnotation(ClearAndReloadCache.class);

		String name = annotation.value();
		Set<String> keys = stringRedisTemplate.keys("*" + name + "*");
		stringRedisTemplate.delete(keys);

		// 执行加入双删注解的改动数据库的业务 即controller中的方法业务
		Object proceed = null;
		try {
			proceed = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		// 开一个线程,延迟1秒,可以根据业务修改
		// 在线程中延迟删除,同时将业务代码的结果返回,这样不影响业务代码的执行
		new Thread(() -> {
			try {
				Thread.sleep(1000);
				Set<String> keys1 = stringRedisTemplate.keys("*" + name + "*");
				stringRedisTemplate.delete(keys1);
				System.out.println("-----------1秒钟后,在线程中延迟删除完毕 -----------");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		return proceed;
	}
}