package com.wy.loggers;

import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * {@link AbstractRequestLoggingFilter}:日志记录解决方案,有两个不同的实现类,通常使用{@link CommonsRequestLoggingFilter}
 * 
 * <pre>
 * 需要设置日志级别:
 * logging:level:'[org.springframework.web.filter.CommonsRequestLoggingFilter]': DEBUG
 * 或
 * logging:level:'[org.springframework.web.filter]': DEBUG
 * 需要加入到FilterRegistrationBean的拦截器中
 * 
 * 若需要自定义日志,可继承CommonsRequestLoggingFilter,重写日志级别
 * 
 * 日志监听入口为{@link LoggingApplicationListener}
 * </pre>
 * 
 * 还可以利用拦截器,AOP,过滤器进行日志记录
 * 
 * SpringBoot3.3利用Actuator调整日志级别:
 * 
 * <pre>
 * 暴露loggers: management.endpoints.web.exposure.include=loggers
 * 将com.example包的日志级别调整为DEBUG:
 * 		curl -X POST http://ip:port/actuator/loggers/com.example -H "Content-Type: application/json" -d '{"configuredLevel":"DEBUG"}'
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2024-10-18 14:01:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class CommonLoggerFilter {

	@Bean
	CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		// 包含查询参数字符串
		loggingFilter.setIncludeQueryString(true);
		// 包含请求体
		loggingFilter.setIncludePayload(true);
		// 包含请求头
		loggingFilter.setIncludeHeaders(true);
		// 包含客户端信息,记录客户端的IP地址和会话信息
		loggingFilter.setIncludeClientInfo(true);
		// 添加日志前缀
		loggingFilter.setAfterMessagePrefix("REQUEST DATA-");
		// 限制请求体长度,确保请求体记录不会影响性能,适合在请求体较大的情况下限制记录内容
		loggingFilter.setMaxPayloadLength(1000);
		return loggingFilter;
	}
}