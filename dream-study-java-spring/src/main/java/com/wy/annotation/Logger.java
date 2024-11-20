package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个测试注解
 *
 * @author 飞花梦影
 * @date 2021-12-09 16:48:40
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Logger {

	/**
	 * 日志描述,对于什么表格进行了什么操作
	 */
	String description() default "";

	/**
	 * 操作了的表名
	 */
	String tableName() default "";
}