package com.wy.valid;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
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
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
				.configure()
				// 快速失败模式
				.failFast(true)
				.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	/**
	 * 将国际化注入到Validation中,默认使用classpath下的ValidationMessages.propertes
	 * 
	 * 在NotNull等注解中直接使用{}可找到国际化文件中的相关属性
	 * 
	 * @param messageSource
	 * @return
	 */
	@Bean
	LocalValidatorFactoryBean validatorFactoryBean(MessageSource messageSource) {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		// 设置国际化
		factoryBean.setValidationMessageSource(messageSource);
		// 设置使用 HibernateValidator 校验器
		factoryBean.setProviderClass(HibernateValidator.class);
		// 设置 快速异常返回 只要有一个校验错误就立即返回失败,其他参数不在校验
		Properties properties = new Properties();
		properties.setProperty("hibernate.validator.fail_fast", "true");
		factoryBean.setValidationProperties(properties);
		// 加载配置
		factoryBean.afterPropertiesSet();
		return factoryBean;
	}

	/**
	 * 默认从{@link AcceptHeaderLocaleResolver}中获取语言,且语言格式为en-us,而不是en_us
	 * 
	 * 如果要自定义获取的方式字段,如下{@link I18nLocaleResolver}
	 * 
	 * @return
	 */
	@Bean
	LocaleResolver localeResolver() {
		return new I18nLocaleResolver();
	}

	/**
	 * 获取请求头国际化信息
	 */
	static class I18nLocaleResolver implements LocaleResolver {

		@Override
		public Locale resolveLocale(HttpServletRequest httpServletRequest) {
			String language = httpServletRequest.getHeader("content-language");
			Locale locale = Locale.getDefault();
			if (StringUtils.isNotBlank(language)) {
				String[] split = language.split("_");
				locale = new Locale(split[0], split[1]);
			}
			return locale;
		}

		@Override
		public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
				Locale locale) {
		}
	}
}