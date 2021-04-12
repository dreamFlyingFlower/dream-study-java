package com.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.listener.S_ApplicationContextInitializer;

/**
 * 自动装配测试类
 * 
 * 若是需要自动注入其他jar包中的类,当本类在Spring的扫描路径中,且类上添加了相关注解,那本类就会生效
 * 
 * 当本类不在Spring的扫描路径中,不管本类有没有添加相关注解,都不会生效,特别是引入其他jar包时,
 * 所以此时就可以借助resources目录下的META-INF/spring.factories,详细写法参考spring-boot-autoconfigure.jar
 * spring.factories中可以添加需要自动注入的类,监听器listener,初始化方法initializers,拦截器fileters等,
 * 不管Spring是否扫描到被添加的类,这些类在启动时都会被添加到Spring组件中
 * 
 * 这些添加到spring.factories中的类可以不添加@Configuration或其他注解,也可以添加,默认都是添加
 * 
 * 用户自定义的组件在spring上下加载完成,但是还没有刷新上下文之后注入到spring上下文中
 * 
 * {@link @SpringBootApplication}:该注解引入了其他注解,主要是引入自动配置
 * {@link SpringApplication#run}:主要是对spring上下文的解析,装配等,同时对SpringBootApplication注解所包含其的其他类进行处理
 * 
 * {@link ConditionalOnClass}:该注解判断当前环境中是否有某个类,有则该注解修饰的方法或类才加载
 * {@link ConditionalOnBean}:作用等同于ConditionalOnClass
 * {@link ConditionalOnMissingClass}:该注解判断当前环境中是否没有某个类,没有则该注解修饰的方法或类才加载
 * {@link ConditionalOnMissingBean}:作用等同于ConditionalOnMissingClass
 * {@link ConditionalOnWebApplication}:该注解判断当前环境是否为一个web应用,是则该注解修饰的类或方法才加载
 * {@link ConditionalOnNotWebApplication}:作用和ConditionalOnWebApplication相反,不是才加载
 * {@link ConditionalOnProperty}:该注解表示指定配置是否存在,即使该指定配置不存在,默认也是生效的
 * 
 * @author ParadiseWY
 * @date 2020-12-02 22:23:38
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class S_AutoConfig {

	@Bean
	public S_ApplicationContextInitializer init() {
		System.out.println("S_AutoConfig...");
		return new S_ApplicationContextInitializer();
	}
}