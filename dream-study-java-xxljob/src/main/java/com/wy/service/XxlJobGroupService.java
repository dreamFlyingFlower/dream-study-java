package com.wy.service;

import com.wy.model.XxlJobGroup;

/**
 * 执行器接口
 * 
 * @author 飞花梦影
 * @date 2022-01-08 00:36:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface XxlJobGroupService {

	/**
	 * 根据appName获得执行器主键ID
	 * 
	 * @param appName 执行器标识
	 * @return 执行器对象
	 */
	XxlJobGroup getJobGroup(String appName);
}