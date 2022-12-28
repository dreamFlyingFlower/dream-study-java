package com.wy.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Conditional;

/**
 * 实现类似Spring {@link ConditionalOnBean}的注解,底层由#ConfigurationClassParser的processConfigurationClass()进行处理
 *
 * @author 飞花梦影
 * @date 2022-12-28 14:33:15
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Conditional(MyCondition.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface MyConditionProperty {

	String name() default "";

	String havingValue() default "";
}