package com.wy.loggers;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Conditions;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.DefaultSink;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Precorrelation;
import org.zalando.logbook.Sink;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

/**
 * Logbook:一个第三方库,能实现完整的请求和响应日志记录,能记录任何http流量
 * 
 * 不同SpringBoot版本使用不同的logbook版本,2.XX使用2.XX,3.XX使用3.XX
 * 
 * 还需要配置日志级别
 * 
 * <pre>
 * logging:
 *   level:
 *     org.zalando.logbook.Logbook: TRACE
 * </pre>
 * 
 * 可以结合logback使用,见logback-logbook.xml
 * 
 * 默认Logbook提供了ClientHttpRequestInterceptor实现,并且注册为了bean,在创建RestTemplate时,只需要将其添加到RestTemplate中即可
 *
 * @author 飞花梦影
 * @date 2025-02-06 10:52:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class LogbookFilter {

	@Bean
	Logbook logbook() {
		return Logbook.builder()
				.condition(
						// 请求路径不记录swagger的
						Conditions.exclude(Conditions.requestTo("/swagger/*"))
								// 必须是application/json请求的
								.and(Conditions.contentType("application/json")))
				// Sink接口可以实现更复杂的用例,例如将请求/响应写入结构化的持久存储
				.sink(new DefaultSink(new DefaultHttpLogFormatter(), new DefaultHttpLogWriter()))
				// 自定义,可将日志写入到数据库
				.sink(new Sink() {

					@Override
					public void write(Correlation correlation, HttpRequest request, HttpResponse response)
							throws IOException {
						System.err.println("==============================");
						System.err.println("request header:\t" + request.getHeaders());
						System.err.println("request body:\t" + request.getBodyAsString());
						System.out.println();
						System.err.println("response header:\t" + response.getHeaders());
						System.err.println("response body:\t" + response.getBodyAsString());
						System.err.println("==============================");
					}

					@Override
					public void write(Precorrelation precorrelation, HttpRequest request) throws IOException {

					}
				})
				.build();
	}

	@Bean
	RestTemplate logbookRestTemplate(LogbookClientHttpRequestInterceptor interceptor) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Arrays.asList(interceptor));
		return restTemplate;
	}
}