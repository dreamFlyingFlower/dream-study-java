package com.wy.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义EasyExcel单元格合并注解
 *
 * @author 飞花梦影
 * @date 2024-04-01 13:59:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyExcelMerge {

	/**
	 * 是否合并单元格
	 *
	 * @return true || false
	 */
	boolean merge() default true;

	/**
	 * 是否为主键,即该字段相同的行合并
	 *
	 * @return true || false
	 */
	boolean isPrimaryKey() default false;
}