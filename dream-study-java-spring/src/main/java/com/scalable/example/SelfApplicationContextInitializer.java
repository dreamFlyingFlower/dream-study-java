package com.scalable.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * {@link ApplicationContextInitializer#initialize(ConfigurableApplicationContext)}:
 * 整个spring容器在刷新之前初始化{@link ConfigurableApplicationContext}的回调接口,就是在容器刷新之前调用此类的initialize方法.
 * 此时容器还未初始化,主要场景为激活一些配置,或利用此时class还没被类加载器加载的时机进行动态字节码注入等操作
 * 
 * 所有ApplicationContextInitializer实现类都在listener的contextPrepared中调用,在上下文准备(refresh)完成之前调用:
 * {@link SpringApplication#run(String...)}
 * ->{@link SpringApplication#prepareContext}
 * -->{@link SpringApplication#applyInitializers}
 * 
 * 因为这时候spring容器还没被初始化,所以想要自己的扩展的生效,有以下三种方式:
 * 
 * <pre>
 * 1.在启动类中用springApplication.addInitializers(new SelfApplicationContextInitializer())语句加入;
 * 2.配置文件配置context.initializer.classes=com.example.demo.TestApplicationContextInitializer
 * 3.在spring.factories中加入org.springframework.context.ApplicationContextInitializer=com.wy.extension.SelfApplicationContextInitializer
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-02 19:51:29
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfApplicationContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext arg0) {
		System.out.println("S_ApplicationContextInitializer...initialize");
	}
}