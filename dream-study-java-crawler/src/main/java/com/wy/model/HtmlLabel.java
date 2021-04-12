package com.wy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HtmlLabel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 解析的数据类型
	 */
	private String type;

	/**
	 * 标签内容
	 */
	private String value;

	/**
	 * 设置样式
	 */
	private String style;
}