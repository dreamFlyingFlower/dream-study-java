package com.wy.login;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 次数限制注解,作用在接口方法上,当超过指定次数之后限制登录
 *
 * @author 飞花梦影
 * @date 2025-09-02 16:41:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitCount {

	/**
	 * 资源名称,用于描述接口功能
	 */
	String name() default "";

	/**
	 * 资源 key
	 */
	String key() default "";

	/**
	 * key prefix
	 *
	 * @return
	 */
	String prefix() default "";

	/**
	 * 时间的,单位秒 默认60s过期
	 */
	int period() default 60;

	/**
	 * 限制访问次数 默认3次
	 */
	int count() default 3;
}