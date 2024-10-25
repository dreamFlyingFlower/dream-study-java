package dream.study.spring.signature;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册过滤器,也可以实现WebMvcConfigurer
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:47:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class FilterConfig {

	@Bean
	@ConditionalOnBean(SignatureProperties.class)
	FilterRegistrationBean<RequestCachingFilter>
			requestCachingFilterRegistration(RequestCachingFilter requestCachingFilter) {
		FilterRegistrationBean<RequestCachingFilter> bean = new FilterRegistrationBean<>(requestCachingFilter);
		bean.setOrder(1);
		return bean;
	}
}