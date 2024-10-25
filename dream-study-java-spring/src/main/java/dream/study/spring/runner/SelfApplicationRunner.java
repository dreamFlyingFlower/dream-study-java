package dream.study.spring.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

/**
 * SpringBoot启动完之后立即调用,可以实现ApplicationRunner或CommandLineRunner,必须注入到Spring组件中
 * 
 * {@link ApplicationArguments}:用来获得启动时的经过封装的参数,在{@link SpringApplication#callRunners}中进行调用
 * 
 * <pre>
 * 假设启动语句为:java -jar test.jar --foo=ab --foo=ac java
 * {@link ApplicationArguments#getSourceArgs()}:被传递给应用程序的原始参数组成的字符串数组.[--foo=ab, --foo=ac, java]
 * {@link ApplicationArguments#getOptionNames()}:获取选项名称的Set字符串集合.[foo]
 * {@link ApplicationArguments#getOptionValues(String)}:通过名称来获取该名称对应的选项值.如参数为foo,则结果为["ab","ac"]
 * {@link ApplicationArguments#containsOption(String)}:用来判断是否包含某个选项的名称.如参数为foo,则结果为[true]
 * {@link ApplicationArguments#getNonOptionArgs()}:来获取所有的无选项参数.["java"]
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-04 16:25:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class SelfApplicationRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("S_ApplicationRunner...springboot所有组件装配完成后立即调用");
	}
}