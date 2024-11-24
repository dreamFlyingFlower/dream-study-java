package com.wy.resolver.parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 由{@link ParameterResolver}作为判断标准进行解析
 *
 * @author 飞花梦影
 * @date 2024-11-23 10:45:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {

}