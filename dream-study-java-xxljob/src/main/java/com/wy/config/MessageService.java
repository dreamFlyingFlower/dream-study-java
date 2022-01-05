package com.wy.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import com.wy.lang.StrTool;
import com.wy.result.ResultException;

/**
 * 国际化
 *
 * @author 飞花梦影
 * @date 2022-01-05 14:36:28
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
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