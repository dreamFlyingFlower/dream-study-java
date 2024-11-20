package com.wy.loggers;

import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * {@link AbstractRequestLoggingFilter}:日志记录解决方案,有两个不同的实现类,通常使用{@link CommonsRequestLoggingFilter}
 * 
 * 需要设置日志级别:logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG,若需要自定义日志,可继承CommonsRequestLoggingFilter,重写日志级别
 * 
 * 日志监听入口为{@link LoggingApplicationListener}
 *
 * @author 飞花梦影
 * @date 2024-10-18 14:01:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class Loggers {

	@Bean
	CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setIncludeHeaders(true);
		filter.setIncludeClientInfo(true);
		filter.setAfterMessagePrefix("REQUEST DATA-");
		return filter;
	}
}