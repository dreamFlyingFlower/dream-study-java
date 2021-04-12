package com.wy.jdks.ex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重复注解的容器类,必须有
 * 
 * @author ParadiseWY
 * @date 2019年8月26日 22:12:47
 * @git {@link https://github.com/mygodness100}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface J8_Annotation_Tests {

	J8_Annotation_Test[] value();
}