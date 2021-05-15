package com.wy.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 通过Import注入实体类的3种方法,可从Import源码查看相关类
 * 
 * @auther 飞花梦影
 * @date 2019-10-09 10:22:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
// @Import(User.class)
// @Import(MyImportSelector.class)
@Import(MyImportBeanDefinitionRegister.class)
public class MyImport {

}