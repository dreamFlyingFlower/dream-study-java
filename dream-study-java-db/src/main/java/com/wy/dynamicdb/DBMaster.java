package com.wy.dynamicdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @apiNote 该注解标注的方法,数据必须从主数据库从中读取
 * @author ParadiseWY
 * @date 2019年12月21日 下午3:25:37
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBMaster {

}