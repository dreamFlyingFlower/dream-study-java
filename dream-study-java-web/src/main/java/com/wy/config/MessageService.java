package com.wy.config;

import java.util.Locale;

import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import com.wy.lang.StrTool;
import com.wy.result.ResultException;

/**
 * 国际化配置使用,自动配置类{@link MessageSourceAutoConfiguration},Spring组件中可以直接注入{@link MessageSource}使用
 * 
 * 在配置文件中配置spring.messages.beanname:i18n/messages,值是国际化文件的地址,必须在classpath下,最后一个表示文件名,messages可自定义.
 * 国际化配置文件一般分三段:文件名_语言缩写_国家缩写.properties,不带任何下划线后缀的是默认文件,即找不到本地化配置文件时使用,
 * 文件名都是相同的,只有后两段不同,可查看{@link java.util.Locale}
 * 
 * 若需要大量的国际化配置文件,放在classpath下是不明智的,可以重写{@link ResourceBundleMessageSource#doGetBundle},从数据库读取配置
 * 
 * @author 飞花梦影
 * @date 2021-01-28 17:28:20
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class MessageService {

	private static MessageSource messageSource;

	public MessageService(MessageSource messageSource) {
		MessageService.messageSource = messageSource;
	}

	private static void assertNull(String msg) {
		if (StrTool.isBlank(msg)) {
			throw new ResultException(getMessage("", "提示消息不能为空"));
		}
	}

	public static String getMessage(String code) {
		String message = messageSource.getMessage(code, null, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public static String getMessage(String code, Object... args) {
		String message = messageSource.getMessage(code, args, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public static String getMessage(String code, Locale locale, Object... args) {
		String message = messageSource.getMessage(code, args, locale);
		assertNull(message);
		return message;
	}

	public static String getMessage(String code, String defaultMessage) {
		String message = messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public static String getMessage(String code, String defaultMessage, Locale locale) {
		String message = messageSource.getMessage(code, null, defaultMessage, locale);
		assertNull(message);
		return message;
	}

	public static String getMessage(String code, String defaultMessage, Object... args) {
		String message = messageSource.getMessage(code, args, defaultMessage, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public static String getMessage(String code, String defaultMessage, Locale locale, Object... args) {
		String message = messageSource.getMessage(code, args, defaultMessage, locale);
		assertNull(message);
		return message;
	}
}