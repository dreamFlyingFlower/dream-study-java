package com.wy.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * {@link BeanDefinitionRegistryPostProcessor}:该接口在读取项目中的beanDefinition之后执行,提供一个补充的扩展点,
 * 可以在这里动态注册自己的beanDefinition,可以加载classpath之外的bean
 *
 * @author 飞花梦影
 * @date 2022-10-17 23:36:16
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition("需要注册的bean class");
		beanDefinitionBuilder.addPropertyValue("注入属性的key", "注入属性的value");
		registry.registerBeanDefinition("beanName", beanDefinitionBuilder.getBeanDefinition());
	}
}