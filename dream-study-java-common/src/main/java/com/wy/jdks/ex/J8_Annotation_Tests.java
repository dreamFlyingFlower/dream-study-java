package com.wy.jdks.ex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重复注解的容器类,必须有
 * 
 * @author 飞花梦影
 * @date 2019-08-26 22:12:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface J8_Annotation_Tests {

	J8_Annotation_Test[] value();
}