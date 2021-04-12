package com.wy.model;

import java.util.List;

import com.wy.enums.CrawlerEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 抓取内容和规则的封装
 * 
 * @author 飞花梦影
 * @date 2021-01-07 12:00:31
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@NoArgsConstructor
public class ParseRule {

	/**
	 * 映射字段
	 */
	private String field;

	/**
	 * URL 校验规则
	 */
	private String urlValidateRegular;

	/**
	 * 解析规则类型
	 */
	private CrawlerEnum.ParseRuleType parseRuleType;

	/**
	 * 规则
	 */
	private String rule;

	/**
	 * 抓取内容列表
	 */
	private List<String> parseContentList;

	/**
	 * 构造方法
	 * 
	 * @param field
	 * @param parseRuleType
	 * @param rule
	 */
	public ParseRule(String field, CrawlerEnum.ParseRuleType parseRuleType, String rule) {
		this.field = field;
		this.parseRuleType = parseRuleType;
		this.rule = rule;
	}

	/**
	 * 检查是否有效,如果内容为空则判断该类为空
	 * 
	 * @return
	 */
	public boolean isAvailability() {
		boolean isAvailability = false;
		if (null != parseContentList && !parseContentList.isEmpty()) {
			isAvailability = true;
		}
		return isAvailability;
	}

	/**
	 * 获取合并后的内容
	 *
	 * @return
	 */
	public String getMergeContent() {
		StringBuilder stringBuilder = new StringBuilder();
		if (null != parseContentList && !parseContentList.isEmpty()) {
			for (String str : parseContentList) {
				stringBuilder.append(str);
			}
		}
		return stringBuilder.toString();
	}
}