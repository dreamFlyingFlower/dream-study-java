package com.wy.service;

import com.wy.model.XxlJobInfo;

/**
 * xxljob调用
 *
 * @author 飞花梦影
 * @date 2022-01-04 17:32:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface XxlJobService {

	/**
	 * 登录
	 * 
	 * @return 登录的cookie
	 */
	String login();

	/**
	 * 新增定时任务
	 * 
	 * @param jobInfo 定时任务参数
	 * @return 新增成功之后的任务编号jobinfoid
	 */
	Integer add(XxlJobInfo jobInfo);
}