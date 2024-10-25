package dream.study.spring.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Spring中一些需要使用Enable开头的注解配置之后才可以使用其他特定注解的注解
 * 
 * 无需再次手动注入的类:
 * 
 * <pre>
 * {@link EnableConfigurationProperties}:自动注入{@link ConfigurationProperties}修饰的配置类,由{@link SpringBootApplication}注入
 * {@link EnableAspectJAutoProxy}:是否使用cglib作为动态代理的关键jar,默认不使用,该注解已由其他注解注入
 * </pre>
 * 
 * 需要手动注入的注解类:
 * 
 * <pre>
 * {@link EnableAsync}:允许使用{@link Async}
 * ->{@link Async}:修饰类或方法,表示类或方法异步执行,相当于开启了一个Runnable线程
 * 
 * {@link EnableWebSecurity}:启用spring-security机制,允许使用{@link EnableGlobalMethodSecurity}
 * ->{@link EnableGlobalMethodSecurity}:对spring-security进行配置
 * 
 * {@link EnableScheduling}:允许使用{@link Scheduled}
 * ->{@link Scheduled}:自动使用定时任务配置
 * </pre>
 * 
 * @auther 飞花梦影
 * @date 2021-05-15 09:12:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Deprecated
public class EnableConfig {

}