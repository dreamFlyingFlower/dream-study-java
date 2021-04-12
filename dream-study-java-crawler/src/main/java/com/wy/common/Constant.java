package com.wy.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 公共配置类
 * 
 * @author wanyang 2018年7月24日
 *
 */
public interface Constant {

	/**
	 * 日志格式化信息
	 */
	String LOG_INFO = "!!!==== {}";

	String LOG_WARN = "@@@===={}";

	String LOG_ERROR = "###===={}";

	/**
	 * 测试mybatis中常量的使用
	 */
	Integer USER_STATE = 1;

	/**
	 * 默认编码集
	 */
	Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
}