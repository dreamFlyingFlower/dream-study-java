package com.wy.config;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局数据管理器,将Date类型和String类型互相转换
 *
 * @author 飞花梦影
 * @date 2022-09-20 14:44:14
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@RestControllerAdvice
public class SelfInitBinder {

	/**
	 * 初始化数据管理器
	 * 
	 * @param dataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
	}
}