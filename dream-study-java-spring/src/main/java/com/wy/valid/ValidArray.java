package com.wy.valid;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.validation.MessageInterpolatorFactory;

/**
 * 自定义注解实现参数校验:校验前端参数传递值是否在指定数值中.message(),groups(),payload()必须有
 * 
 * {@link Constraint}:指定解析当前注解的类
 *
 * @author ParadiseWY
 * @date 2020-12-19 10:05:17
 * @git {@link https://github.com/mygodness100}
 */
@Documented
@Constraint(validatedBy = { ValidArrayValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ValidArray {

	/**
	 * 不符合校验规则的提示信息,需要写在ValidationMessages.properties中,是以国际化文件
	 * 详见@{@link ValidationAutoConfiguration#defaultValidator}
	 * ->{@link MessageInterpolatorFactory} ->{@link ParameterMessageInterpolator}
	 * ->{@link AbstractMessageInterpolator}
	 * 
	 * 如果是固定信息,不需要以{}包裹
	 * 
	 * 默认值属性会从默认的国际化文件ValidationMessages.properties中查找
	 * 
	 * @return class类
	 */
	String message() default "{dream.study.spring.valid.ValidArray.message}";

	/**
	 * 何时使用本校验类
	 * 
	 * @return 接口
	 */
	Class<?>[] groups() default {};

	/**
	 * 参数传递的数据类型
	 * 
	 * @return 类
	 */
	Class<? extends Payload>[] payload() default {};

	/**
	 * 参数传递的指定值数组
	 * 
	 * @return 数组
	 */
	int[] vals() default {};
}