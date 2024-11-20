package com.wy.experience.example;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.wy.annotation.Logger;

/**
 * 自定义包扫描,参照{@link ClassPathScanningCandidateComponentProvider}
 * 
 * <pre>
 * {@link ClassPathScanningCandidateComponentProvider}:可以对指定包进行扫描并注入,见{@link EurekaServerAutoConfiguration#jerseyApplication}
 * {@link ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters)}: 使用默认规则(注解方式)进行包扫描,见构造注释
 * {@link FilterType}:扫描时的方式,默认为 {@link FilterType#ANNOTATION}
 * {@link TypeFilterParser$FilterType}:各种扫描拦截类型解析器
 * {@link AnnotationTypeFilter}:扫描时指定扫描拦截器,例子见{@link FeignClientsRegistrar#registerFeignClients}
 * </pre>
 *
 * @author 飞花梦影
 * @date 2022-09-18 11:09:42
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyScan {

	public static void main(String[] args) throws Exception {
		// 使用默认规则(FilterType.ANNOTATION)扫描包:false,不使用默认规则
		ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider =
				new ClassPathScanningCandidateComponentProvider(false);
		// 创建注解类型的过滤器
		TypeFilter annotationTypeFilter = new AnnotationTypeFilter(Logger.class);
		System.out.println(annotationTypeFilter);
		// 创建Aspect类型过滤器
		TypeFilter typeFilter = new AspectJTypeFilter("过滤器表达式,参照AOP的表达式即可", MyScan.class.getClassLoader());
		// 添加到扫描器中
		classPathScanningCandidateComponentProvider.addIncludeFilter(typeFilter);
		// 定义要扫描类的全限定类名集合
		Set<String> hashSet = new HashSet<>();
		// 通过配置文件获得
		Properties properties = PropertiesLoaderUtils.loadAllProperties("xxxx.properties");
		String basePackageStr = properties.getProperty("base-packages");
		// 需要扫描的包路径,可通过配置文件或其他方式导入
		String[] basePackages = basePackageStr.split(",");
		// 填充集合中的内容
		for (String basePackage : basePackages) {
			classPathScanningCandidateComponentProvider.findCandidateComponents(basePackage)
					.forEach(beanDefinition -> hashSet.add(beanDefinition.getBeanClassName()));
		}

		// ClassPathScanningCandidateComponentProvider的子类,多了scan(),可以直接扫描包,并利用BeanDefinitionRegistry进行注入
		ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner =
				new ClassPathBeanDefinitionScanner(new SimpleBeanDefinitionRegistry(), false);
		classPathBeanDefinitionScanner.scan("basePackages");
	}
}