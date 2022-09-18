package com.wy.registerbean;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.wy.scan.MyScan;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-09-18 15:42:40
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyImportBeanDefinitionRegisterScan implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// 创建注解类型的过滤器
		// TypeFilter annotationTypeFilter = new AnnotationTypeFilter(Logger.class);
		// System.out.println(annotationTypeFilter);
		try {
			// 通过配置文件获得
			Properties properties = PropertiesLoaderUtils.loadAllProperties("xxxx.properties");
			// 创建Aspect类型过滤器:需要扫描的包的表达式,可以写死也可以从配置获取,参照AOP表达式;ClassLoader
			TypeFilter typeFilter = new AspectJTypeFilter("com.wy.*", MyScan.class.getClassLoader());
			// 使用默认规则(FilterType.ANNOTATION)扫描包:false,不使用默认规则
			ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner =
					new ClassPathBeanDefinitionScanner(registry, false);
			// 添加到扫描器中
			classPathBeanDefinitionScanner.addIncludeFilter(typeFilter);
			String basePackageStr = properties.getProperty("base-packages");
			// 需要扫描的包路径,可通过配置文件或其他方式导入
			String[] basePackages = basePackageStr.split(",");
			// 扫描指定包
			classPathBeanDefinitionScanner.scan(basePackages);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}