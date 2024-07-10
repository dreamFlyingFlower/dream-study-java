package com.wy.valid;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * Validator配置
 * 
 * {@link RequestResponseBodyMethodProcessor}:解析{@link RequestBody}和{@link ResponseBody},进行方法参数校验
 * {@link RequestResponseBodyMethodProcessor#resolveArgument}:解析参数
 * {@link RequestResponseBodyMethodProcessor#readWithMessageConverters}:将请求数据封装到指定对象类型中
 * {@link RequestResponseBodyMethodProcessor#validateIfApplicable}:校验参数
 * 
 * {@link MethodValidationInterceptor}:普通方法参数拦截,由MethodValidationPostProcessor引入
 *
 * @author 飞花梦影
 * @date 2024-07-09 10:09:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class ValidatorConfig {

	/**
	 * 默认SpringValidation会校验完所有字段,然后才抛出异常,使用快速失败模式,一旦校验失败就立即返回
	 * 
	 * @return Validator
	 */
	@Bean
	Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
				// 快速失败模式
				.failFast(true).buildValidatorFactory();
		return validatorFactory.getValidator();
	}
}