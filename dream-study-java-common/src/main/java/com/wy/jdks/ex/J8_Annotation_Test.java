package com.wy.jdks.ex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重复注解的例子,需要添加重复注解的注解标识,同时制定重复注解所需要的容器.
 * 在使用的时候需要用反射类的getAnnotationsByType方法,而且返回的是一个数组类型
 * 
 * @author ParadiseWY
 * @date 2019年8月26日 22:25:36
 * @git {@link https://github.com/mygodness100}
 */
@Repeatable(J8_Annotation_Tests.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface J8_Annotation_Test {

	String value();
}