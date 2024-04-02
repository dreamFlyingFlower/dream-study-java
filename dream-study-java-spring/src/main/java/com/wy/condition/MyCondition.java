package com.wy.condition;

import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 实现自定义条件解析方法,参照 {@link SpringBootCondition}或其他Condition实现类
 * 
 * {@link Condition}:实现该接口可以自定义类在满足某些特定条件时才被注册
 * 
 * {@link ConditionContext}:条件上下文
 * 
 * <pre>
 * {@link ConditionContext#getRegistry()}:返回bean定义注册器,可以通过注册器获取bean定义的各种配置信息
 * {@link ConditionContext#getBeanFactory()}:返回ConfigurableListableBeanFactory类型的bean工厂,相当于一个ioc容器对象
 * {@link ConditionContext#getEnvironment()}:返回当前spring容器的环境配置信息对象
 * {@link ConditionContext#getResourceLoader()}:返回资源加载器
 * {@link ConditionContext#getClassLoader()}:返回类加载器
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-12-28 14:34:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyCondition implements Condition {

	/**
	 * 判断是否满足注入Bean的条件
	 * 
	 * @param context 上下文
	 * @param metadata 用来获取被{@link Conditional}标注的对象上的所有注解信息
	 * @return 是否匹配
	 */
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return false;
	}
}