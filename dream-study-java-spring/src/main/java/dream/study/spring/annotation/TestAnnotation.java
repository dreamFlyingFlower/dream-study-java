package dream.study.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试注解
 *
 * @author 飞花梦影
 * @date 2022-06-19 23:15:31
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface TestAnnotation {

	String value() default "";
}