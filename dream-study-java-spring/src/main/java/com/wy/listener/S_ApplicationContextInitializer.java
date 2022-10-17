package com.wy.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 所有ApplicationContextInitializer实现类都在listener的contextPrepared中调用,在上下文准备(refresh)完成之前调用:
 * {@link SpringApplication#run(String...)}
 * ->{@link SpringApplication#prepareContext}
 * -->{@link SpringApplication#applyInitializers}
 * 
 * 不需要将实现了ApplicationContextInitializer接口的类注入到Spring中,因为在autoconfigure包中已经加入了自动配置
 * org.springframework.context.ApplicationContextInitializer,但是要将实现类添加到spring.factories中
 * 
 * {@link ApplicationContextInitializer}是整个spring容器在刷新之前初始化{@link ConfigurableApplicationContext}的回调接口,
 * 此时容器还未初始化,主要场景为激活一些配置,或利用此时class还没被类加载器加载的时机进行动态字节码注入等操作
 * 
 * @author ParadiseWY
 * @date 2020-12-02 19:51:29
 * @git {@link https://github.com/mygodness100}
 */
public class S_ApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext arg0) {
		System.out.println("S_ApplicationContextInitializer...initialize");
	}
}