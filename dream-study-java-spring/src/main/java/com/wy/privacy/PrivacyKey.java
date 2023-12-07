package com.wy.privacy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法上添加,判断方法是否需要数据脱敏服务
 *
 * @author 飞花梦影
 * @date 2023-12-07 14:59:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface PrivacyKey {

	/**
	 * 是否启用序列化脱敏 默认开启
	 */
	boolean isKey() default true;

	/**
	 * 是否为分页对象
	 */
	boolean isPageKey() default false;
}