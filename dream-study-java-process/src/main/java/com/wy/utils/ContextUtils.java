package com.wy.utils;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 在普通类中使用spring注解类
 * 
 * @author wanyang 2018年1月15日
 */
@Component
public class ContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;
	}

	/**
	 * 获取applicationContext对象
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据bean的name来查找对象
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	/**
	 * 根据bean的class来查找对象 此处源码会查找出多个同类,若只有一个则正常返回,若是有多个则抛异常
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 根据bean的name和class来查找对象 先根据bean的名称来查找对象,再比对class,相当于单独使用nama和class的集成版
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) {
		return applicationContext.getBean(beanName, clazz);
	}

	/**
	 * 根据name和参数来查找此组件,若找不到,会向父类延伸
	 */
	public static Object getBean(String beanName, Object... args) {
		return applicationContext.getBean(beanName, args);
	}

	/**
	 * 根据组件的name获得这个组件的字节码类型
	 */
	public static Class<?> getType(String beanName) {
		return applicationContext.getType(beanName);
	}

	/**
	 * 根据bean的name判断是否有这个组件
	 */
	public static boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	/**
	 * 根据bean的name拿到这个组件的别名,可能有多个
	 */
	public static String[] getAliases(String beanName) {
		return applicationContext.getAliases(beanName);
	}

	/**
	 * 根据name来判断此组件是否为单例,true是,false否
	 */
	public static boolean isSingleton(String beanName) {
		return applicationContext.isSingleton(beanName);
	}

	/**
	 * 根据name来判断此组件是否为原型,true是,false否
	 */
	public static boolean isPrototype(String beanName) {
		return applicationContext.isPrototype(beanName);
	}

	/**
	 * 根据name来判断此组件和给定的字节码类是否是同一个实例,若是为true
	 */
	public static boolean isTypeMatch(String beanName, Class<?> clazz) {
		return applicationContext.isTypeMatch(beanName, clazz);
	}

	/**
	 * 根据bean的class来查找所有的对象(包括子类)
	 */
	public static <T> Map<String, ? extends Object> getBeansByClass(Class<T> c) {
		return applicationContext.getBeansOfType(c);
	}
}