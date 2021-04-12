package com.wy.dynamicdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标注的方法,数据必须从主数据库从中读取
 *
 * @author 飞花梦影
 * @date 2019-12-21 15:25:42
 * @git {@link https://github.com/mygodness100}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBMaster {

}