package com.wy.study;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

/**
 * 自定义区域语言解析器,在{@link WebMvcAutoConfiguration.EnableWebMvcConfiguration#localeResolver()}初始化
 * 
 * 当前自定义方式不会被页面中语言选项所修改
 * 
 * @author ParadiseWY
 * @date 2020-11-30 15:24:38
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class MyLocaleResolver implements LocaleResolver {

	/**
	 * 从url中获得区域语言标识,可自定义,假设当前标识为lang,该参数的值同国际化配置文件,不带文件名和后缀的部分
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String lang = request.getParameter("lang");
		// 默认的本地语言
		Locale locale = Locale.getDefault();
		if (StringUtils.isEmptyOrWhitespace(lang)) {
			return locale;
		}
		String[] split = lang.split("_");
		return new Locale(split[0], split[1]);
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

	}
}