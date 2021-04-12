package com.wy.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 解析封装对象,定义了转换后对象的骨架
 * 
 * @author 飞花梦影
 * @date 2021-01-07 12:20:44
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public abstract class ParseItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 处理类型 有正向 反向两种 FORWARD, 正向 REVERSE 反向
	 */
	private String handelType;

	/**
	 * 文档抓取类型
	 */
	private String documentType;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 获取初始的URL
	 * 
	 * @return
	 */
	public abstract String getInitialUrl();

	/**
	 * 获取需要处理的内容
	 * 
	 * @return
	 */
	public abstract String getParserContent();
}