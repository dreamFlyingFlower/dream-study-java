package com.wy.config;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * 一个项目中只能有一个类继承{@link WebMvcConfigurationSupport},只会扫描继承该类的第一类中的方法
 * 
 * Filter,Intercpetor,Listener等初始化,最好实现WebMvcConfigurer而不继承{@link WebMvcConfigurationSupport},有可能会改变json序列化方式
 *
 * @author 飞花梦影
 * @date 2022-01-17 15:43:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class GlobalWebConfig implements WebMvcConfigurer {

	/**
	 * 处理跨域请求,该方法最终会被{@link DefaultCorsProcessor#processRequest}中调用
	 * 
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH").allowCredentials(true).maxAge(3600);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 支持swagger2资源库
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE).addResourceHandler("doc.html", "swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE).addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		// 第三个资源库支持静态资源访问 前端页面
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE).addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/");
	}

	/**
	 * 自动序列化{@link LocalDate}, {@link LocalDateTime}
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		// 解决Spring Boot LocalDateTime格式处理
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addSerializer(LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

		// 忽略没有的字段
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				// 取消timestamps形式
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(javaTimeModule)
				.registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module());
		converter.setObjectMapper(mapper);
		converters.add(converter);
		converters.add(new BufferedImageHttpMessageConverter());// 增加图片转换,影响展示图片
	}
}