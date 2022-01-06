package com.wy.common;

/**
 * 不可修改公共配置类
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:50:45
 * @git {@link https://github.com/mygodness100}
 */
public interface Constants {

	/** 登录xxljob-admin时存储的Cookie Key */
	String XXLJOB_COOKIE_LOGIN_KEY = "XXL_JOB_LOGIN_IDENTITY";

	/** 添加定时任务URL */
	String XXLJOB_URL_API_ADD = "/jobinfo/add";

	/** 更新定时任务URL */
	String XXLJOB_URL_API_UPDATE = "/jobinfo/update";

	/** 删除定时任务URL */
	String XXLJOB_URL_API_REMOVE = "/jobinfo/remove";

	/** 开始定时任务URL */
	String XXLJOB_URL_API_START = "/jobinfo/start";

	/** 执行一次定时任务URL */
	String XXLJOB_URL_API_TRIGGER = "/jobinfo/trigger";

	/** 停止定时任务URL */
	String XXLJOB_URL_API_STOP = "/jobinfo/stop";
}