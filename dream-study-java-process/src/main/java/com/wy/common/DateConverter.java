package com.wy.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * 全局时间检测,日期转换器
 * 需要配合在spring的配置文件中使用响应的配置
 * 
 * @author wanyang
 */
public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String arg0) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setLenient(false);
		try {
			return sdf.parse(arg0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}