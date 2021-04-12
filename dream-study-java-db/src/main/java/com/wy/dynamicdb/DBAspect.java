package com.wy.dynamicdb;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 数据库进行读写分离的切面,Order注解保证该aop在Transaction之前执行
 *
 * @author 飞花梦影
 * @date 2019-12-21 23:47:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Order(-1)
@Aspect
@Configuration
public class DBAspect {

	@Pointcut("!@annotation(com.wy.dynamicdb.DBMaster) " + "&& (execution(* com.wy.service..*.select*(..)) "
			+ "|| execution(* com.wy.service..*.get*(..)))")
	public void readPointcut() {}

	@Pointcut("@annotation(com.wy.dynamicdb.DBMaster) " + "|| execution(* com.wy.service..*.insert*(..)) "
			+ "|| execution(* com.wy.service..*.add*(..)) " + "|| execution(* com.wy.service..*.update*(..)) "
			+ "|| execution(* com.wy.service..*.edit*(..)) " + "|| execution(* com.wy.service..*.delete*(..)) "
			+ "|| execution(* com.wy.service..*.remove*(..))")
	public void writePointcut() {}

	@Before("readPointcut()")
	public void read() {
		DynamicSourceHolder.setSalve();
	}

	@Before("writePointcut()")
	public void write() {
		DynamicSourceHolder.setMaster();
	}
}