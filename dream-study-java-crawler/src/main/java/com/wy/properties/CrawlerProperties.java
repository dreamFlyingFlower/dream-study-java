package com.wy.properties;

import java.io.Serializable;
import java.util.List;

import com.wy.model.ParseRule;

import lombok.Getter;
import lombok.Setter;

/**
 * 爬虫配置相关属性
 */
@Setter
@Getter
public class CrawlerProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 需要爬取的uri */
	private String urlPrefix;

	/** 需要爬取的url后缀 */
	private List<String> urlSuffix;

	/** 初始化请求 */
	private List<String> initCrawlerUrlList;

	/** 初始化抓取的xpath表达式 */
	private String initCrawlerXpath;

	/** 帮助页面抓取规则 */
	private String helpCrawlerXpath;

	/** 是否开启帮助页面分页抓取 */
	private Integer crawlerHelpNextPagingSize;

	/** 目标页抓取规则 */
	private List<ParseRule> targetParseRuleList;
}