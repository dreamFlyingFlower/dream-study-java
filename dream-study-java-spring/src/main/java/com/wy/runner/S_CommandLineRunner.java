package com.wy.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SpringBoot启动完之后立即调用,此时已经加载完所有的配置,可以直接使用Spring中的组件,必须注入到Spring中
 * 
 * 重写方法中的参数即在启动jar包时出入的参数,没有经过 任何包装,输入什么就是什么
 * 
 * @author ParadiseWY
 * @date 2020-12-02 21:59:19
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class S_CommandLineRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		for (String string : args) {
			System.out.println(string);
		}
		System.out.println("S_CommandLineRunner...springboot所有组件装配完成后立即调用");
	}
}