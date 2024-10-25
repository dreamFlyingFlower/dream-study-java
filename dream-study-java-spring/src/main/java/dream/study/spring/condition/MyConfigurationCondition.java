package dream.study.spring.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 效果和{@link Condition}相同,但是{@link ConfigurationCondition}可以控制Bean在哪个Spring容器初始化的哪个阶段被注册,参照#OnBeanCondition
 * 
 * {@link ConfigurationPhase}:设置配置类在什么阶段被解析
 * 一个配置类被Spring处理有2个阶段:配置类解析阶段、bean注册阶段(将配置类作为bean被注册到spring容器).
 * 如果将{@link Condition}的实现类作为配置类上{@link Conditional}中,那么这个条件会对两个阶段都有效,无法精细的控制某个阶段的.
 * 如果想控制某个阶段,比如可以让他解析,但是不能让他注册,就可以实现{@link ConfigurationCondition}
 *
 * @author 飞花梦影
 * @date 2024-04-02 14:26:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyConfigurationCondition implements ConfigurationCondition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return false;
	}

	/**
	 * 指定Bean在哪个阶段被注册,返回空则2个阶段都会被注册一次,当然只有一次生效
	 * 
	 * @return 注册阶段
	 */
	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.REGISTER_BEAN;
	}
}