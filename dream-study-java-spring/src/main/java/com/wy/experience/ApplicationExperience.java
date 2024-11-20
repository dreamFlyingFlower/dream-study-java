package com.wy.experience;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import com.wy.experience.example.MyScan;

/**
 * 可借鉴实现代码
 * 
 * <pre>
 * 1.实现拦截某个注解,而不直接使用Interceptor,Aspect等,参照
 * 		{@link AsyncAnnotationBeanPostProcessor},{@link AsyncAnnotationAdvisor},{@link AnnotationAsyncExecutionInterceptor}
 * 
 * 2.实现拦截某个注解,而不直接使用Interceptor,Aspect等,同上,但是实现过程有区别,上述方式可能出现循环依赖问题,参照
 * 		{@link AnnotationAwareAspectJAutoProxyCreator} 
 * 
 * 3.同时引入多种Bean操作,根据类型选择适合的Bean操作,参照
 * 		{@link CacheAutoConfiguration}->{@link CacheAutoConfiguration.CacheConfigurationImportSelector},{@link PasswordEncoderFactories}->{@link DelegatingPasswordEncoder}
 * 
 * 4.自定义包扫描,参照
 * 		{@link ClassPathScanningCandidateComponentProvider},{@link EurekaServerAutoConfiguration#jerseyApplication},
 * 		eg:{@link MyScan}
 * 
 * 5.扫描时指定扫描拦截器,参照
 * 		{@link AnnotationTypeFilter},{@link FeignClientsRegistrar#registerFeignClients}
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ApplicationExperience {

	public static void main(String[] args) {

	}
}