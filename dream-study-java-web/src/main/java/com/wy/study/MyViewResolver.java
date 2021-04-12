package com.wy.study;

import java.util.Locale;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.devtools.remote.server.Dispatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * 自定义视图解析器,需要实现{@link ViewResolver},从{@link Dispatcher}中可以看到自定义的解析器,
 * 在{@link WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#viewResolver()}中被初始化
 * 
 * @author ParadiseWY
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