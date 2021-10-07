package com.wy.jdks.ex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重复注解的例子,需要添加重复注解的注解标识,同时制定重复注解所需要的容器.
 * 在使用的时候需要用反射类的getAnnotationsByType(),而且返回的是一个数组类型
 * 
 * @author 飞花梦影
 * @date 2019-08-26 22:25:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Repeatable(J8_Annotation_Tests.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface J8_Annotation_Test {

	String value();
}