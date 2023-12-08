package com.wy.dynamicdb;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多数据源切换时需要的切面注解,根据value切换到对应数据源
 * 
 * @author 飞花梦影
 * @date 2021-01-13 14:55:36
 * @git {@link https://github.com/mygodness100}
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DSChoice {

	DSType value() default DSType.MASTER;
}