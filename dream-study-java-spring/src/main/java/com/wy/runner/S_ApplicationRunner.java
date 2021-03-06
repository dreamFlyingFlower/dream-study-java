package com.wy.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * SpringBoot启动完之后立即调用,可以实现ApplicationRunner或CommandLineRunner,必须注入到Spring组件中
 * 
 * @apiNote ApplicationArguments对象用来获得启动时的经过封装的参数:<br>
 *          假设启动语句为:java -jar test.jar --foo=ab --foo=ac java<br>
 *          getSourceArgs():被传递给应用程序的原始参数组成的字符串数组.[--foo=ab, --foo=ac, java]<br>
 *          getOptionNames():获取选项名称的Set字符串集合.[foo]
 *          getOptionValues("foo"):通过名称来获取该名称对应的选项值.["ab","ac"]
 *          containsOption("foo"):用来判断是否包含某个选项的名称<br>
 *          .[true] getNonOptionArgs():用来获取所有的无选项参数.["java"]
 * 
 * @apiNote CommandLineRunner的参数直接就是启动的参数,未进行任何封装
 * 
 * @author ParadiseWY
 * @date 2020-11-04 16:25:11
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class S_ApplicationRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("S_ApplicationRunner...springboot所有组件装配完成后立即调用");
	}
}