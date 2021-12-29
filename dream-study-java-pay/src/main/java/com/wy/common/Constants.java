package com.wy.common;

import org.springframework.util.ClassUtils;

public interface Constants {

	String SF_FILE_SEPARATOR = System.getProperty("file.separator");// 文件分隔符

	String SF_LINE_SEPARATOR = System.getProperty("line.separator");// 行分隔符

	String SF_PATH_SEPARATOR = System.getProperty("path.separator");// 路径分隔符

	String QRCODE_PATH =
			ClassUtils.getDefaultClassLoader().getResource("static").getPath() + SF_FILE_SEPARATOR + "qrcode";

	// 微信账单 相关字段 用于load文本到数据库
	String WEIXIN_BILL =
			"tradetime, ghid, mchid, submch, deviceid, wxorder, bzorder, openid, tradetype, tradestatus, bank, currency, totalmoney, redpacketmoney, wxrefund, bzrefund, refundmoney, redpacketrefund, refundtype, refundstatus, productname, bzdatapacket, fee, rate";

	String PATH_BASE_INFO_XML = SF_FILE_SEPARATOR + "WEB-INF" + SF_FILE_SEPARATOR + "xmlConfig" + SF_FILE_SEPARATOR;

	String CURRENT_USER = "UserInfo";

	String SUCCESS = "success";

	String FAIL = "fail";

	/**
	 * 微信基础接口地址
	 */
	// 获取token接口(GET)
	String TOKEN_URL =
			"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// oauth2授权接口(GET)
	String OAUTH2_URL =
			"https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	// 刷新access_token接口（GET）
	String REFRESH_TOKEN_URL =
			"https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	// 菜单创建接口（POST）
	String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 菜单查询（GET）
	String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

	// 菜单删除（GET）
	String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 微信支付接口地址
	 */
	// 微信支付统一接口(POST)
	String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	// 微信退款接口(POST)
	String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	// 订单查询接口(POST)
	String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	// 关闭订单接口(POST)
	String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";

	// 退款查询接口(POST)
	String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

	// 对账单接口(POST)
	String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";

	// 短链接转换接口(POST)
	String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";

	// 接口调用上报接口(POST)
	String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

	/** 支付宝签名方式 */
	public static String SIGN_TYPE = "RSA2";

	/** 支付宝参数类型 */
	public static String PARAM_TYPE = "json";

	/** 默认编码 */
	public static String CHARSET = "utf-8";
}