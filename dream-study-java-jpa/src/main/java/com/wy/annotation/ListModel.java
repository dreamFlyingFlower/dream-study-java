package com.wy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wy.base.AbstractPager;

/**
 * 分页专用
 * 
 * @author 飞花梦影
 * @date 2021-04-01 09:14:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ListModel {

	/**
	 * 指定分页类
	 * 
	 * @return 分页类
	 */
	Class<? extends AbstractPager> value();
}