package com.wy.listener;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SpringApplicationRunListener:监听器,所有的监听器实现类都在{@link SpringApplication#run}中调用
 * 
 * 实现SpringApplicationRunListener时,必须添加一个带2个参数的构造,否则启动失败.
 * SpringApplicationRunListener的实现类不需要手动添加到Spring上下文中,在Spring启动时会自动装配实现类
 * 
 * @author ParadiseWY
 * @date 2020-12-02 19:50:24
 * @git {@link https://github.com/mygodness100}
 */
public class S_SpringApplicationRunListener implements SpringApplicationRunListener {

	/**
	 * 构造器,必须有,且参数类型固定,可参照其他listener
	 * 
	 * @param application
	 * @param args
	 */
	public S_SpringApplicationRunListener(SpringApplication application, String[] args) {
	}

	/**
	 * spring启动时调用
	 */
	@Override
	public void starting() {
		SpringApplicationRunListener.super.starting();
		System.out.println("S_ApplicationRunListener...starting");
	}

	/**
	 * 运行环境已经准备好
	 */
	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		SpringApplicationRunListener.super.environmentPrepared(environment);
		System.out.println("S_ApplicationRunListener...environmentPrepared");
	}

	/**
	 * IOC容器已经准备好
	 */
	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {
		SpringApplicationRunListener.super.contextPrepared(context);
		System.out.println("S_ApplicationRunListener...contextPrepared");
	}

	/**
	 * IOC容易已经加载完成,但是还没有刷新上下文
	 */
	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {
		SpringApplicationRunListener.super.contextLoaded(context);
		System.out.println("S_ApplicationRunListener...contextLoaded");
	}

	/**
	 * IOC上下文已经刷新,但是还没有调用{@link CommandLineRunner CommandLineRunners} 和
	 * {@link ApplicationRunner ApplicationRunners}
	 */
	@Override
	public void started(ConfigurableApplicationContext context) {
		SpringApplicationRunListener.super.started(context);
		System.out.println("S_ApplicationRunListener...started");
	}

	/**
	 * IOC容器马上完成,此时{@link CommandLineRunner CommandLineRunners}和
	 * {@link ApplicationRunner ApplicationRunners}也已经调用
	 */
	@Override
	public void running(ConfigurableApplicationContext context) {
		SpringApplicationRunListener.super.running(context);
		System.out.println("S_ApplicationRunListener...running");
	}

	/**
	 * SpringBoot启动失败
	 */
	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		SpringApplicationRunListener.super.failed(context, exception);
		System.out.println("S_ApplicationRunListener...failed");
	}
}