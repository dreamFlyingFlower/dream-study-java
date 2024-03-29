package com.wy.signature;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:44:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface PointCutDef {

	@Pointcut("execution(public * com.wy..controller.*.*(..))")
	default void controllerMethod() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	default void postMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	default void getMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	default void putMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	default void deleteMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	default void requestMapping() {
	}

	@Pointcut("controllerMethod() && (requestMapping() || postMapping() || getMapping() || putMapping() || deleteMapping())")
	default void apiMethod() {
	}
}