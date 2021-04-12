package com.wy.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.wy.result.ResultException;
import com.wy.utils.StrUtils;

/**
 * 国际化配置使用
 * 
 * @author 飞花梦影
 * @date 2021-01-28 17:28:20
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class MessageService {

	@Autowired
	private MessageSource messageSource;

	private void assertNull(String msg) {
		if (StrUtils.isBlank(msg)) {
			throw new ResultException(getMessage("", "提示消息不能为空"));
		}
	}

	public String getMessage(String code) {
		String message = messageSource.getMessage(code, null, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public String getMessage(String code, Object... args) {
		String message = messageSource.getMessage(code, args, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public String getMessage(String code, Locale locale, Object... args) {
		String message = messageSource.getMessage(code, args, locale);
		assertNull(message);
		return message;
	}

	public String getMessage(String code, String defaultMessage) {
		String message = messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public String getMessage(String code, String defaultMessage, Locale locale) {
		String message = messageSource.getMessage(code, null, defaultMessage, locale);
		assertNull(message);
		return message;
	}

	public String getMessage(String code, String defaultMessage, Object... args) {
		String message = messageSource.getMessage(code, args, defaultMessage, Locale.getDefault());
		assertNull(message);
		return message;
	}

	public String getMessage(String code, String defaultMessage, Locale locale, Object... args) {
		String message = messageSource.getMessage(code, args, defaultMessage, locale);
		assertNull(message);
		return message;
	}
}