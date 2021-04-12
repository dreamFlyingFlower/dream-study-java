package com.wy.enums;

public class CrawlerEnum {

	/**
	 * 处理方式FORWARD正向,REVERSE 反向
	 */
	public enum HandelType {
		FORWARD,
		REVERSE
	}

	/**
	 * 抓取规则类型
	 */
	public enum ParseRuleType {
		REGULAR,
		XPATH,
		CSS
	}

	/**
	 * 返回数据类型
	 */
	public enum ReturnDataType {
		HTML,
		JSON
	}

	/**
	 * 抓取文档类型<br>
	 * INIT 初始化类型<br>
	 * HELP 列表页<br>
	 * PAGE 需要抓取的页面<br>
	 */
	public enum DocumentType {
		INIT,
		HELP,
		PAGE,
		OTHER
	}

	/**
	 * 抓取类型
	 */
	public enum ComponentType {
		NORMAL,
		PAGEPROCESSOR,
		PIPELINE,
		DOWNLOAD,
		SCHEDULER
	}

	public enum HtmlType {

		P_TAG("p", "text"),
		A_TAG("a", "link"),
		IMG_TAG("img", "image"),
		H1_TAG("h1", "text"),
		H2_TAG("h2", "text"),
		H3_TAG("h3", "text"),
		H4_TAG("h4", "text"),
		H5_TAG("h5", "text"),
		H6_TAG("h6", "text"),
		PRE_TAG("pre", "text"),
		CODE_TAG("code", "code"),
		STRONG_TAG("strong", "text");

		/**
		 * 标签名称
		 */
		private String labelName;

		/**
		 * 数据类型
		 */
		private String dataType;

		HtmlType(String labelName, String dataType) {
			this.labelName = labelName;
			this.dataType = dataType;
		}

		public String getLabelName() {
			return labelName;
		}

		public String getDataType() {
			return dataType;
		}
	}
}