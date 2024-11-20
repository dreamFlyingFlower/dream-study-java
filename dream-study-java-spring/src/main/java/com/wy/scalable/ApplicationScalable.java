package com.wy.scalable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.wy.listener.SelfSpringEvent;
import com.wy.runner.SelfApplicationRunner;
import com.wy.runner.SelfCommandLineRunner;
import com.wy.scalable.example.SelfApplicationContextAwareProcessor;
import com.wy.scalable.example.SelfApplicationContextInitializer;
import com.wy.scalable.example.SelfBeanDefinitionRegistryPostProcessor;
import com.wy.scalable.example.SelfBeanFactoryAware;
import com.wy.scalable.example.SelfBeanFactoryPostProcessor;
import com.wy.scalable.example.SelfBeanNameAware;
import com.wy.scalable.example.SelfDisposableBean;
import com.wy.scalable.example.SelfFactoryBean;
import com.wy.scalable.example.SelfInitializingBean;
import com.wy.scalable.example.SelfInstantiationAwareBeanPostProcessor;
import com.wy.scalable.example.SelfSmartInitializingSingleton;
import com.wy.scalable.example.SelfSmartInstantiationAwareBeanPostProcessor;

/**
 * Spring上下文初始化,可扩展点调用顺序
 * 
 * <pre>
 * {@link ApplicationContextInitializer#initialize(ConfigurableApplicationContext)}:eg:{@link SelfApplicationContextInitializer}
 * {@link AbstractApplicationContext#refresh()}:刷新上下文,加载Bean定义,注解,等
 * {@link BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry()}:eg:{@link SelfBeanDefinitionRegistryPostProcessor}
 * {@link BeanDefinitionRegistryPostProcessor#postProcessBeanFactory(ConfigurableListableBeanFactory)}:eg:{@link SelfBeanDefinitionRegistryPostProcessor}
 * {@link BeanFactoryPostProcessor#postProcessBeanFactory(ConfigurableListableBeanFactory)}:eg:{@link SelfBeanFactoryPostProcessor}
 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInitialization(Object, String)}:eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link SmartInstantiationAwareBeanPostProcessor#determineCandidateConstructors(Class, String)}:eg:{@link SelfSmartInstantiationAwareBeanPostProcessor}
 * {@link MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition()}
 * {@link InstantiationAwareBeanPostProcessor#postProcessAfterInitialization(Object, String)}:eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference(Object, String)}:eg:{@link SelfSmartInstantiationAwareBeanPostProcessor}
 * {@link BeanFactoryAware#setBeanFactory(BeanFactory)}:eg:{@link SelfBeanFactoryAware}
 * {@link InstantiationAwareBeanPostProcessor#postProcessPropertyValues()}:eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link ApplicationContextAwareProcessor#invokeAwareInterfaces()}:eg:{@link SelfApplicationContextAwareProcessor}
 * {@link BeanNameAware#setBeanName(String)}:eg:{@link SelfBeanNameAware}
 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInitialization(Object, String)}:eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link PostConstruct}:在bean初始化阶段,先调用{@link BeanPostProcessor#postProcessBeforeInitialization()}之后,
 * 		再调用被PostConstruct修饰的方法,最后调用{@link InitializingBean#afterPropertiesSet()}
 * {@link InitializingBean#afterPropertiesSet()}:eg:{@link SelfInitializingBean}
 * {@link InstantiationAwareBeanPostProcessor#postProcessAfterInitialization(Object, String)}:eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link FactoryBean#getObject()}:eg:{@link SelfFactoryBean}
 * {@link SmartInitializingSingleton#afterSingletonsInstantiated()}:eg:{@link SelfSmartInitializingSingleton}
 * {@link ApplicationRunner#run()}:整个项目全部准备完成,等待接收请求时执行触发,eg:{@link SelfApplicationRunner}
 * {@link CommandLineRunner#run(String...)}:调用完所有ApplicationRunner之后调用,eg:{@link SelfCommandLineRunner}
 * {@link DisposableBean#destroy()}:当此对象销毁时触发.比如说运行applicationContext.registerShutdownHook时,就会触发这个方法,eg:{@link SelfDisposableBean}
 * 
 * {@link ApplicationListener}:事件监听,见{@link SelfSpringEvent},有多个Spring自带的事件发布对象可以监听
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ApplicationScalable {

	public static void main(String[] args) {
	}
}