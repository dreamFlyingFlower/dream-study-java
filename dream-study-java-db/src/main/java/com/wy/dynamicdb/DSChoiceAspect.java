package com.wy.dynamicdb;

import java.lang.reflect.Method;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 根据{@link DSChoice}选择进行拦截数据源
 * 
 * @author 飞花梦影
 * @date 2021-01-13 14:57:50
 * @git {@link https://github.com/mygodness100}
 */
@Aspect
@Order(1)
@Component
public class DSChoiceAspect {

	@Pointcut("@annotation(com.wy.dynamicdb.DSChioce)" + "|| @within(com.wy.dynamicdb.DSChoice)")
	public void dataSourcePoint() {

	}

	@Around("dataSourcePoint()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		DSChoice dataSource = getDataSource(point);
		if (Objects.nonNull(dataSource)) {
			DynamicSourceHolder.setDataSourceKey(dataSource.value());
		}
		try {
			return point.proceed();
		} finally {
			DynamicSourceHolder.clear();
		}
	}

	/**
	 * 获取需要切换的数据源
	 */
	public DSChoice getDataSource(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Class<? extends Object> targetClass = point.getTarget().getClass();
		DSChoice targetDataSource = targetClass.getAnnotation(DSChoice.class);
		if (Objects.nonNull(targetDataSource)) {
			return targetDataSource;
		} else {
			Method method = signature.getMethod();
			DSChoice dataSource = method.getAnnotation(DSChoice.class);
			return dataSource;
		}
	}
}