package com.wy.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 所有初始化实现类都在listener的contextPrepared中调用,在上下文准备完成之前调用:<br>
 * {@link SpringApplication#run(String...)}->{@link SpringApplication#prepareContext}
 * ->{@link SpringApplication#applyInitializers}
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