package com.wy.condition;

import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 实现自定义条件解析方法,参照 {@link SpringBootCondition}或其他Condition实现类
 *
 * @author 飞花梦影
 * @date 2022-12-28 14:34:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return false;
	}
}