package dream.study.spring.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;

/**
 * 实现类似Spring{@link ConditionalOnBean}的注解,底层由#ConfigurationClassParser的processConfigurationClass()进行处理
 * 
 * #OnBeanCondition:是{@link ConditionalOnBean},{@link ConditionalOnSingleCandidate},{@link ConditionalOnMissingBean}三个注解的处理类
 * {@link ConfigurationPhase}:标识某个Condition应该在哪个阶段执行
 * {@link ConfigurationPhase#PARSE_CONFIGURATION}:当前的Condition在配置类解析时执行.如果该Condition返回false,则该配置类不会被解析
 * {@link ConfigurationPhase#REGISTER_BEAN}:当前的Condition在注册bean时执行
 *
 * @author 飞花梦影
 * @date 2022-12-28 14:33:15
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Conditional(MyCondition.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface MyConditionProperty {

	String name() default "";

	String havingValue() default "";
}