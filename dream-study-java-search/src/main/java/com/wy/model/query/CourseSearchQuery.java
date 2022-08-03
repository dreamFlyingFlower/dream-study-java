package com.wy.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程案例搜索参数
 *
 * @author 飞花梦影
 * @date 2022-08-03 16:28:06
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSearchQuery {

	/**
	 * 关键字
	 */
	String keyword;

	/**
	 * 一级分类
	 */
	String mt;

	/**
	 * 二级分类
	 */
	String st;

	/**
	 * 难度等级
	 */
	String grade;

	/**
	 * 价格区间
	 */
	Float price_min;

	Float price_max;

	/**
	 * 排序字段
	 */
	String sort;

	/**
	 * 过虑字段
	 */
	String filter;
}