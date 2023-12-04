package com.wy.mybatis.sensitive;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解定义在类上,插件通过扫描类对象是否包含这个注解来决定是否继续扫描其中的字段注解,需要配合EncryptData
 * 
 * @author 飞花梦影
 * @date 2023-11-20 13:37:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Inherited
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {

}