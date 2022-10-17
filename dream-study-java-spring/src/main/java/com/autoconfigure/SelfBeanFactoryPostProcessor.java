package com.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * {@link BeanFactoryPostProcessor}:该接口是beanFactory的扩展接口,在spring读取beanDefinition信息之后,实例化bean之前调用.
 * 可以通过实现这个扩展接口来自行处理一些东西,比如修改已经注册的beanDefinition的元信息
 *
 * @author 飞花梦影
 * @date 2022-10-17 23:37:56
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}
}