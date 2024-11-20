package com.wy.registerbean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.wy.experience.example.MyScan;

/**
 * 利用注解或切面扫描指定包下的类注入Bean,该类可以加{@link Configuration}之类注解或利用{@link Import}注解使用
 *
 * @author 飞花梦影
 * @date 2022-09-18 15:11:12
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyImportSelectorScan implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// 使用默认规则(FilterType.ANNOTATION)扫描包:false,不使用默认规则
		ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider =
				new ClassPathScanningCandidateComponentProvider(false);
		// 创建注解类型的过滤器
		// TypeFilter annotationTypeFilter = new AnnotationTypeFilter(Logger.class);
		// System.out.println(annotationTypeFilter);
		try {
			// 通过配置文件获得
			Properties properties = PropertiesLoaderUtils.loadAllProperties("xxxx.properties");
			// 创建Aspect类型过滤器:需要扫描的包的表达式,可以写死也可以从配置获取,参照AOP表达式;ClassLoader
			TypeFilter typeFilter = new AspectJTypeFilter("com.wy.*", MyScan.class.getClassLoader());
			// 添加到扫描器中
			classPathScanningCandidateComponentProvider.addIncludeFilter(typeFilter);
			// 定义要扫描类的全限定类名集合
			Set<String> hashSet = new HashSet<>();
			String basePackageStr = properties.getProperty("base-packages");
			// 需要扫描的包路径,可通过配置文件或其他方式导入
			String[] basePackages = basePackageStr.split(",");
			// 填充集合中的内容
			for (String basePackage : basePackages) {
				classPathScanningCandidateComponentProvider.findCandidateComponents(basePackage)
						.forEach(beanDefinition -> hashSet.add(beanDefinition.getBeanClassName()));
			}
			return basePackages;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}