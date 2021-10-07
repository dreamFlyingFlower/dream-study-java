package com.wy.study;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.springframework.format.Formatter;

import com.wy.enums.DateEnum;
import com.wy.lang.StrTool;

/**
 * 自定义时间格式转换器,将字符串转成 LocalDateTime.不严谨,只是例子
 * 
 * @author 飞花梦影
 * @date 2021-10-01 16:31:50
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyFormatter implements Formatter<LocalDateTime> {

	/**
	 * 输出,将LocalDateTime转换为String
	 */
	@Override
	public String print(LocalDateTime object, Locale locale) {
		if (Objects.isNull(object)) {
			return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateEnum.DATETIME.getPattern()));
		}
		return object.format(DateTimeFormatter.ofPattern(DateEnum.DATETIME.getPattern()));
	}

	@Override
	public LocalDateTime parse(String text, Locale locale) throws ParseException {
		if (StrTool.isNotBlank(text) && text.length() == 21) {
			if (' ' == text.charAt(10)) {
				return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DateEnum.DATETIME.getPattern()));
			} else if ('T' == text.charAt(10)) {
				return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
			}
		}
		if (StrTool.isNotBlank(text) && text.length() == 10) {
			return LocalDateTime.of(LocalDate.parse(text, DateTimeFormatter.ofPattern(DateEnum.DATE.getPattern())),
					LocalTime.parse("00:00:00"));
		}
		return null;
	}
}