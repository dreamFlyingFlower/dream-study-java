package com.wy.shiro.constant;

/**
 * shiro的常量类
 *
 * @author 飞花梦影
 * @date 2022-06-21 23:06:03
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class ShiroConstant {

	/** 后台系统 */
	public static final String PLATFORM_MGT = "platform_mgt";

	/** 前端系统 */
	public static final String OPEN_API = "open_api";

	/** 密码登陆 */
	public final static String LOGIN_TYPE_PASSWORD = "login_type_password";

	/** 快捷登陆 */
	public final static String LOGIN_TYPE_QUICK = "login_type_quick";

	/** pc密码登陆 */
	public final static String LOGIN_TYPE_PASSWORD_PC = "login_type_password_pc";

	/** 未登录 */
	public static final Integer NO_LOGIN_CODE = 1;

	public static final String NO_LOGIN_MESSAGE = "请先进行登录";

	/** 登录成功 */
	public static final Integer LOGIN_SUCCESS_CODE = 2;

	public static final String LOGIN_SUCCESS_MESSAGE = "登录成功";

	/** 登录失败 */
	public static final Integer LOGIN_FAILURE_CODE = 3;

	public static final String LOGIN_FAILURE_MESSAGE = "登录失败";

	/** 缺少用户权限 */
	public static final Integer NO_AUTH_CODE = 5;

	public static final String NO_AUTH_MESSAGE = "权限不足";

	/** 缺少用户角色 */
	public static final Integer NO_ROLE_CODE = 6;

	public static final String NO_ROLE_MESSAGE = "用户角色不符合";
}