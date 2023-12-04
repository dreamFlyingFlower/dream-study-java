package com.wy.mybatis.sensitive;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏字段.有两种使用方式:配合@SensitiveData加在类中的字段上;直接在Mapper中的方法参数上使用
 *
 * @author 飞花梦影
 * @date 2023-11-20 13:41:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptData {

}