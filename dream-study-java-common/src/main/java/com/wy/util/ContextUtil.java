package com.wy.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * Spring上下文,可在非Spring环境中获得Spring组件
 * 
 * @author 飞花梦影
 * @date 2021-08-04 17:29:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class ContextUtil implements InitializingBean, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	// 该类为spring特有类,可以拿到反射中方法的形参名.jdk1.8之前是拿不到的.jdk8如何拿要百度咯
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER;

	static {
		PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
	}

	public static ParameterNameDiscoverer getDisCoverer() {
		return PARAMETER_NAME_DISCOVERER;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ContextUtil.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * 判断applicationContext中是否包含指定beanName的bean
	 * 
	 * @param beanName spring组件的name
	 * @return true->存在,false->不存在
	 */
	public static boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	/**
	 * 根据bean的name拿到这个组件的别名,可能有多个
	 * 
	 * @param beanName spring组件的name
	 * @return 组件别名
	 */
	public static String[] getAliases(String beanName) {
		return applicationContext.getAliases(beanName);
	}

	/**
	 * 获取applicationContext对象
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据bean的class来查找对象
	 * 
	 * @param clazz 需要查找的类字节码
	 * @return 对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 根据bean的beanName来查找对象
	 * 
	 * @param beanName spring组件中定义的value,若是不写,默认为类名首字母小写
	 * @return 对象,需强转
	 */
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * 根据bean的name和class来查找对象.先根据bean的名称来查找对象,再比对class,相当于单独使用nama和class的集成版
	 * 
	 * @param beanName spring组件的name
	 * @param clazz bean字节码
	 * @return bean实例
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) {
		return applicationContext.getBean(beanName, clazz);
	}

	/**
	 * 根据beanName和参数来查找组件,若找不到,会向父类延伸
	 * 
	 * @param beanName spring组件的name
	 * @param args 参数
	 * @return 组件
	 */
	public static Object getBean(String beanName, Object... args) {
		return applicationContext.getBean(beanName, args);
	}

	/**
	 * 根据bean的class来查找所有的对象(包括子类)
	 * 
	 * @param clazz 父类字节码
	 * @return 所有子类
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz);
	}

	/**
	 * 根据beanName返回该组件的具体字节码类
	 * 
	 * @param beanName spring组件的name
	 * @return Class 注册对象的类型
	 */
	public static Class<?> getType(String beanName) {
		return applicationContext.getType(beanName);
	}

	/**
	 * 根据beanName来判断此组件是否为原型
	 * 
	 * @param beanName spring组件的name
	 * @return true->是,false->否
	 */
	public static boolean isPrototype(String beanName) {
		return applicationContext.isPrototype(beanName);
	}

	/**
	 * 根据beanName来判断此组件是否为单例
	 * 
	 * @param beanName spring组件的name
	 * @return true->是,false->否
	 */
	public static boolean isSingleton(String beanName) {
		return applicationContext.isSingleton(beanName);
	}

	/**
	 * 根据beanName来判断此组件和给定的字节码类是否是同一个实例,若是为true
	 * 
	 * @param beanName spring组件的name
	 * @param clazz 类字节码
	 * @return true->是,false->否
	 */
	public static boolean isTypeMatch(String beanName, Class<?> clazz) {
		return applicationContext.isTypeMatch(beanName, clazz);
	}
}