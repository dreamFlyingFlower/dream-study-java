package com.wy.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

/**
 * SpringBoot启动完之后立即调用,此时已经加载完所有的配置,可以直接使用Spring中的组件,必须注入到Spring中
 * 
 * {@link CommandLineRunner}:该接口的参数直接就是启动的参数,不进行任何封装.在{@link SpringApplication#callRunners}中进行调用
 * 
 * @author 飞花梦影
 * @date 2020-12-02 21:59:19
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class SelfCommandLineRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		for (String string : args) {
			System.out.println(string);
		}
		System.out.println("S_CommandLineRunner...springboot所有组件装配完成后立即调用");
	}
}