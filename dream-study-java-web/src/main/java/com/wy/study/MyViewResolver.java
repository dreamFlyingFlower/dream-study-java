package com.wy.study;

import java.util.Locale;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.devtools.remote.server.Dispatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.thymeleaf.spring5.view.AbstractThymeleafView;
import org.thymeleaf.spring5.view.ThymeleafView;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * 自定义视图解析器,需要实现{@link ViewResolver},从{@link Dispatcher}中可以看到自定义的解析器,
 * 在{@link WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#viewResolver()}中被初始化
 * 
 * 相关接口和类:
 * 
 * <pre>
 * {@link AbstractCachingViewResolver}:缓存相关
 * {@link ContentNegotiatingViewResolver}:根据返回类型选择合适的视图解析器
 * {@link FreeMarkerView},{@link FreeMarkerViewResolver}:FreeMarker相关视图
 * {@link ThymeleafView},{@link ThymeleafViewResolver},{@link AbstractThymeleafView}:Thymeleaf相关视图
 * {@link UrlBasedViewResolver},{@link AbstractUrlBasedView}:与视图的URL相关
 * {@link InternalResourceView},{@link InternalResourceViewResolver}:内置的解析器,处理JSP,JSTL
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-30 13:23:05
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class MyViewResolver implements ViewResolver {

	@Override
	public View resolveViewName(String arg0, Locale arg1) throws Exception {
		return null;
	}
}