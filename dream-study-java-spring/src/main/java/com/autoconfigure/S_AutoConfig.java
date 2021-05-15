package com.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;

import com.wy.config.ConfigProperties;
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
 * {@link SpringBootApplication}:多注解的合体,扫描自动配置类,加载spring上下文
 * ->{@link SpringBootApplication#scanBasePackages()}:指定包扫描路径,默认扫描当前包以及子包,同{@link ComponentScan}
 *
 * ->{@link EnableAutoConfiguration}:扫描加载自动配置类,会自动加载所有META-INF/spring.factories中配置的相关类
 * -->{@link AutoConfigurationPackage}:导入一个注册类,该注册类将获取运行@SpringBootApplication注解的包及相关信息
 * -->{@link Import}:将一个类注入到Spring中,功能和{@link Configuration}相同,在自定义自动注入类时会使用,通常修饰其他注解
 * 
 * --->{@link ImportSelector}:实现该接口的方法返回类的全路径,需要用{@link Import}引入,和Import不同的是该接口引入Class字符串
 * ---->{@link AutoConfigurationImportSelector#isEnabled}:从配置文件中查找spring.boot.enableautoconfiguration,默认true自动配置
 * ---->{@link AutoConfigurationImportSelector#selectImports()}:引入自动配置
 * ----->{@link AutoConfigurationImportSelector#getAutoConfigurationEntry()}
 * ------>{@link AutoConfigurationImportSelector#getCandidateConfigurations()}
 * ------->{@link SpringFactoriesLoader#loadFactoryNames()}
 * -------->{@link SpringFactoriesLoader#loadSpringFactories()}:从META/spring.factories获得自动装配类,加载到spring上下文中
 * 
 * {@link SpringApplication#run}:主要是对spring上下文的解析,装配等,同时对EnableAutoConfiguration引入的其他类进行处理
 * 
 * {@link Condition}:接口,判断在启动是否加载某个类,配合Conditional注解使用
 * {@link Conditional}:配合Condition使用,判断{@link Condition#matches}是否返回true来决定注解修饰的方法或类是否注册到Spring中
 * {@link ConditionalOnClass}:该注解判断当前环境中是否有某个类,有则该注解修饰的方法或类才加载
 * {@link ConditionalOnBean}:作用等同于ConditionalOnClass
 * {@link ConditionalOnMissingClass}:该注解判断当前环境中是否没有某个类,没有则该注解修饰的方法或类才加载
 * {@link ConditionalOnMissingBean}:作用等同于ConditionalOnMissingClass
 * {@link ConditionalOnWebApplication}:该注解判断当前环境是否为一个web应用,是则该注解修饰的类或方法才加载
 * {@link ConditionalOnNotWebApplication}:作用和ConditionalOnWebApplication相反,不是才加载
 * {@link ConditionalOnProperty}:该注解表示指定配置存在且等于某个给定值时类生效.若配置不存在,默认不生效
 * ->{@link ConditionalOnProperty#value()}:等同于name(),需要检查的配置中的属性名
 * ->{@link ConditionalOnProperty#prefix()}:需要检查的配置前缀,可写可不写
 * ->{@link ConditionalOnProperty#havingValue()}:检查value()指定属性的值是否equals该方法指定的值,true->配置生效,false->不生效
 * ->{@link ConditionalOnProperty#matchIfMissing()}:当value()指定属性的值不存在或错误时的行为,true->仍然加载,默认false->不加载
 * 
 * 其他自动注入配置见{@link ConfigProperties}
 * 
 * @author 飞花梦影
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