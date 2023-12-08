package com.wy.dynamicdb;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 根据DBMaster读写分离的切面,Order注解保证该aop在Transaction之前执行
 *
 * @author 飞花梦影
 * @date 2019-12-21 23:47:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Order(-1)
@Aspect
@Configuration
public class DSMasterAspect {

	@Pointcut("!@annotation(com.wy.dynamicdb.DSMaster) " + "&& (execution(* com.wy.service..*.select*(..)) "
			+ "|| execution(* com.wy.service..*.get*(..)))")
	public void readPointcut() {
	}

	@Pointcut("@annotation(com.wy.dynamicdb.DSMaster) " + "|| execution(* com.wy.service..*.insert*(..)) "
			+ "|| execution(* com.wy.service..*.add*(..)) " + "|| execution(* com.wy.service..*.update*(..)) "
			+ "|| execution(* com.wy.service..*.edit*(..)) " + "|| execution(* com.wy.service..*.delete*(..)) "
			+ "|| execution(* com.wy.service..*.remove*(..))")
	public void writePointcut() {
	}

	@Around("readPointcut()")
	public Object read(ProceedingJoinPoint point) {
		DynamicSourceHolder.setSalve();
		try {
			return point.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		} finally {
			DynamicSourceHolder.clear();
		}
	}

	@Around("writePointcut()")
	public Object write(ProceedingJoinPoint point) {
		DynamicSourceHolder.setMaster();
		try {
			return point.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		} finally {
			DynamicSourceHolder.clear();
		}
	}
}