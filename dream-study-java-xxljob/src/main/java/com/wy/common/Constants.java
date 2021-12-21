package com.wy.common;

/**
 * 不可修改公共配置类
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:50:45
 * @git {@link https://github.com/mygodness100}
 */
public interface Constants {

	/** 日志格式化信息 */
	String LOG_INFO = "!!!==== {}";

	String LOG_WARN = "@@@===={}";

	String LOG_ERROR = "###===={}";

	/** 密码存入数据库的MD5加密的密钥 */
	String SECRET_KEY_DB = "#$%^&*()$%^#$%^&";

	/** 默认分页时每页显示数据量 */
	int PAGE_SIZE = 10;

	/** 地球半径平均值,千米 */
	double EARTH_RADIUS = 6371.004;

	// /**
	// * 通用成功标识
	// */
	// public static final String SUCCESS = "0";
	//
	// /**
	// * 通用失败标识
	// */
	// public static final String FAIL = "1";
	//
	// /**
	// * 登录成功
	// */
	// public static final String LOGIN_SUCCESS = "Success";
	//
	// /**
	// * 注销
	// */
	// public static final String LOGOUT = "Logout";
	//
	// /**
	// * 登录失败
	// */
	// public static final String LOGIN_FAIL = "Error";
	//
	// /**
	// * 验证码 redis key
	// */
	// public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
	//
	// /**
	// * 登录用户 redis key
	// */
	// public static final String LOGIN_TOKEN_KEY = "login_tokens:";
	//
	// /**
	// * 验证码有效期（分钟）
	// */
	// public static final Integer CAPTCHA_EXPIRATION = 2;
	//
	/** 用户token */
	String TOKEN = "token";

	/** 令牌前缀 */
	String TOKEN_PREFIX = "Bearer ";
	//
	// /**
	// * 令牌前缀
	// */
	// public static final String LOGIN_USER_KEY = "login_user_key";
	//
	// /**
	// * 用户ID
	// */
	// public static final String JWT_USERID = "userid";
	//
	// /**
	// * 用户名称
	// */
	// public static final String JWT_USERNAME = Claims.SUBJECT;
	//
	// /**
	// * 用户头像
	// */
	// public static final String JWT_AVATAR = "avatar";
	//
	// /**
	// * 创建时间
	// */
	// public static final String JWT_CREATED = "created";
	//
	// /**
	// * 用户权限
	// */
	// public static final String JWT_AUTHORITIES = "authorities";
	//
}