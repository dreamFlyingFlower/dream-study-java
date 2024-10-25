package dream.study.spring.apicontrol;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API版本控制:header.ApiVersionCondition和path.ApiVersionCondition只使用一个即可
 *
 * @author 飞花梦影
 * @date 2023-10-12 17:02:17
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ApiVersion {

	// 默认接口版本号1.0开始,多级可在正则进行控制
	String value() default "1.0";
}