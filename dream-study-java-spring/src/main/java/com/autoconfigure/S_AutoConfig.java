package com.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;

import com.wy.annotation.S_Annotation;
import com.wy.listener.S_ApplicationContextInitializer;

/**
 * 自动装配测试类,通常自动配置类都是只有一个启动类,在META-INF/spring.factories中加入需要启动的类,引入需要使用的jar
 * 
 * 若是需要自动注入其他jar包中的类,当本类在Spring的扫描路径中,且类上添加了相关注解,那本类就会生效
 * 
 * 当本类不在Spring的扫描路径中,不管本类有没有添加相关注解,都不会生效,特别是引入其他jar包时,
 * 所以此时就可以借助resources目录下的META-INF/spring.factories,详细写法参考spring-boot-autoconfigure.jar
 * spring.factories中可以添加需要自动注入的类,监听器listener,初始化方法initializers,拦截器fileters等,
 * 不管Spring是否扫描到被添加的类,这些类在启动时都会被添加到Spring组件中
 * 
 * 这些添加到spring.factories中的类可以不添加{@link Configuration}或其他注解,也可以添加,默认都是添加
 * 
 * 用户自定义的组件在spring上下加载完成,但是还没有刷新上下文之后注入到spring上下文中
 * 
 * SpringBootApplication启动注解:
 * 
 * <pre>
 * {@link SpringBootApplication}:多注解的合体,扫描自动配置类,加载spring上下文
 * 		exclude():启动时需要排除的自动注入类,同 EnableAutoConfiguration 的exclude
 * 		excludeName():启动时需要排除的自动注入类名,同 EnableAutoConfiguration 的excludeName
 * 		scanBasePackages():启动时进行扫描的包名,不配置则默认扫描当前类以及子类,同 ComponentScan 
 * 		scanBasePackageClasses():启动时进行扫描的特殊类,同 ComponentScan 
 * ->{@link SpringBootConfiguration:作用等同于Configuration,只是起一个标识作用
 * 
 * ->{@link EnableAutoConfiguration}:扫描加载自动配置类,会自动加载所有META-INF/spring.factories中配置的相关类
 * -->{@link AutoConfigurationPackage}:导入一个注册类,该注册类将获取运行@SpringBootApplication注解的包及相关信息
 * -->{@link Import}:将一个类注入到Spring中,需要添加Configuration,在自定义自动注入类时会使用,通常修饰其他注解
 * --->{@link AutoConfigurationImportSelector}:实现 ImportSelector 接口,自动引入
 * 		isEnabled():从配置文件中查找spring.boot.enableautoconfiguration,默认true自动配置
 * 		selectImports():引入自动配置
 * 		getAutoConfigurationEntry():装在自动配置相关类
 * 		getCandidateConfigurations():从指定路径中获得需要自动加载的类
 * ---->{@link SpringFactoriesLoader#loadFactoryNames()}
 * ---->{@link SpringFactoriesLoader#loadSpringFactories()}:从META/spring.factories获得自动装配类,加载到spring上下文
 * 
 * --->{@link ImportSelector}:实现该接口的方法返回类的全路径,需要Import引入,和Import不同的是该接口引入Class字符串
 * 
 * {@link SpringApplication#run}:主要是对Spring上下文的解析,装配等,同时对EnableAutoConfiguration引入的其他类进行处理
 * </pre>
 * 
 * 启动流程:
 * 
 * <pre>
 * 判断是否web环境
 * 加载所有classpath下的META-INF/spring.factories的ApplicationContextInitizlizer
 * 加载所有classpath下的META-INF/spring.factories的ApplicationListener
 * 加载main方法所在的类
 * 开始执行run方法
 * 设置java.awt.headless系统变量
 * 加载所有META-INF/spring.factories的SpringApplicationRunListener
 * 执行所有SpringApplicationRunListener的started方法
 * 实例化ApplicationArguments对象
 * 创建environment
 * 配置environment,将run方法的参数配置到environment
 * 执行SpringApplicationRunListener的environmentPrepared方法
 * 如果不是web环境,但是是web的environment,把这个web才environment转换成标准的environment
 * 打印Banner
 * 初始化ApplicationContext,如果是web环境,则实例化AnnotationConfigEmbeddedWebApplicationContext对象,否则实例化AnnotationConfigApplicationContext
 * 如果beanNameGenerator不为空,将beanNameGenerator注入到context中
 * 回调所有的ApplicationContextInitializer方法
 * 执行SpringApplicationRunListener的contextPrepared方法
 * 依次往spring容器中注入ApplicationArguments,Banner
 * 加载所有的源到context中
 * 执行所有的SpringApplicationRunListener的contextLoaded方法
 * 执行context的refresh方法,并且调用contenxt的registerShutdownHook方法
 * 回调,获取容器中所有的ApplicationRunner,CommandLineRunner接口,然后排序,依次调用
 * 执行所有的SpringApplicationRunListener的finished方法
 * </pre>
 * 
 * 其他自动注入配置见{@link S_Annotation}
 * 
 * @author 飞花梦影
 * @date 2020-12-02 22:23:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class S_AutoConfig {

	@Bean
	public S_ApplicationContextInitializer init() {
		System.out.println("S_AutoConfig...");
		return new S_ApplicationContextInitializer();
	}
}