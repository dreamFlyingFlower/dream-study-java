package com.wy.annotation;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;

/**
 * 自定义{@link ComponentScan} 规则扫描过滤器
 *
 * @author 飞花梦影
 * @date 2022-06-19 18:37:47
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfTypeFilter extends AbstractTypeHierarchyTraversingFilter {

	/**
	 * 定义路径校验的对象
	 */
	private PathMatcher pathMatcher;

	/**
	 * 定义需要从配置文件获取的值. 注意:此处的数据应该是读取配置文件获取,之所以不能使用@Value读取properties配置,因为负责填充属性值的
	 * {@link InstantiationAwareBeanPostProcessor}与 {@link TypeFilter}实例创建根本没关联
	 */
	private String configName;

	/**
	 * 定义可以处理类的包名,指定的package下的.同configName,只能从配置文件获取
	 */
	private String handlerPackage;

	public SelfTypeFilter() {
		this(false, false);
	}

	/**
	 * 构造
	 * 
	 * @param considerInherited 是否考虑基类上的信息
	 * @param considerInterfaces 是否考虑接口上的信息
	 */
	public SelfTypeFilter(boolean considerInherited, boolean considerInterfaces) {
		super(considerInherited, considerInterfaces);
		// 借助spring默认的resource通配符路径方式
		pathMatcher = new AntPathMatcher();
		try {
			// 读取配置文件,硬编码读取配置文件
			Properties properties = PropertiesLoaderUtils.loadAllProperties("config.properties");
			// 从配置文件读取指定值赋值给指定参数
			configName = properties.getProperty("configName");
			// 从配置文件读取可以处理类的包名,例如com.wy.service.*.*
			handlerPackage = ClassUtils.convertClassNameToResourcePath(properties.getProperty("handlerPackage"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 本类将注册为exclude,返回true表示拒绝
	 * 
	 * @param className 指定的类
	 * @return true->拒绝,false->执行
	 */
	@Override
	public boolean matchClassName(String className) {
		// 将类名转换为资源路径,以匹配是否符合扫描条件
		String path = ClassUtils.convertClassNameToResourcePath(className);
		// 判断className是否指定包下的类
		if (!pathMatcher.match(handlerPackage, path)) {
			// 不符合规则路径
			return false;
		}
		try {
			// 判断当前区域和配置的区域是否一致,不一致则不能注册到spring的ioc容器中
			Class<?> clazz = ClassUtils.forName(className, SelfTypeFilter.class.getClassLoader());
			// 获取指定注解
			TestAnnotation testAnnotation = clazz.getAnnotation(TestAnnotation.class);
			// 判断是否有该注解,没有就返回false
			if (testAnnotation == null) {
				return false;
			}
			// 如果有,判断该值与配置文件中的值是否相同,相同返回true,不同返回false
			return !configName.equalsIgnoreCase(testAnnotation.value());
		} catch (ClassNotFoundException | LinkageError e) {
			e.printStackTrace();
		}
		return false;
	}
}