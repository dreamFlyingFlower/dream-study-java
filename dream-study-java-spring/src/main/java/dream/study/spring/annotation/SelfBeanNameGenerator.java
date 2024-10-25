package dream.study.spring.annotation;

import java.beans.Introspector;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 自定义beanName生成规则,参照 {@link AnnotationBeanNameGenerator},在 {@link ComponentScan#nameGenerator()}中使用
 *
 * @author 飞花梦影
 * @date 2022-06-16 23:42:13
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanNameGenerator implements BeanNameGenerator {

	private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";

	// 元注解信息缓存
	private final Map<String, Set<String>> metaAnnotationTypesCache = new ConcurrentHashMap<>();

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		// 定义bean的名称
		String beanName = null;
		// 判断当前bean的定义信息是否是注解的
		if (definition instanceof AnnotatedBeanDefinition) {
			// 将definition转成注解的定义信息
			AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
			// 获取注解bean定义的元信息
			AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
			// 获取定义信息中的所有注解
			Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
			// 遍历types集合
			for (String annotationType : annotationTypes) {
				// 得到注解的属性
				AnnotationAttributes annotationAttributes =
						AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(annotationType, false));
				// 判断attributes是否为null,同时必须是Component及其衍生注解
				if (annotationAttributes != null) {
					// 从缓存中取元注解信息
					Set<String> metaTypes = this.metaAnnotationTypesCache.computeIfAbsent(annotationType, key -> {
						Set<String> result = annotationMetadata.getMetaAnnotationTypes(key);
						return (result.isEmpty() ? Collections.emptySet() : result);
					});
					if (isStereotypeWithNameValue(annotationType, metaTypes, annotationAttributes)) {
						Object value = annotationAttributes.get("value");
						if (value instanceof String) {
							String strVal = (String) value;
							if (StringUtils.hasLength(strVal)) {
								if (beanName != null && !strVal.equals(beanName)) {
									throw new IllegalStateException("Stereotype annotations suggest inconsistent "
											+ "component names: '" + beanName + "' versus '" + strVal + "'");
								}
								beanName = strVal;
							}
						}
					}
				}
			}
		}
		return beanName != null ? beanName : buildDefaultBeanName(definition);
	}

	/**
	 * 判断是否为Component或符合标准的注解,见{@link AnnotationBeanNameGenerator#isStereotypeWithNameValue}
	 * 
	 * @param annotationType 待判断的注解类
	 * @param metaAnnotationTypes 给定注释上的元注释的名称
	 * @param attributes 待判断注解的属性值
	 * @return true->符合,false->不符合
	 */
	protected boolean isStereotypeWithNameValue(String annotationType, Set<String> metaAnnotationTypes,
			@Nullable Map<String, Object> attributes) {
		boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME)
				|| metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)
				|| annotationType.equals("javax.annotation.ManagedBean") || annotationType.equals("javax.inject.Named");
		return (isStereotype && attributes != null && attributes.containsKey("value"));
	}

	/**
	 * 生成一个默认的beanName实现
	 * 
	 * @param definition bean定义
	 * @return beanName
	 */
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String beanClassName = definition.getBeanClassName();
		Assert.state(beanClassName != null, "No bean class name set");
		String shortClassName = ClassUtils.getShortName(beanClassName);
		return Introspector.decapitalize(shortClassName);
	}
}