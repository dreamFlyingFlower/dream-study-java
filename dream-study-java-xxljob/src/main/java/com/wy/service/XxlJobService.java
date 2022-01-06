package com.wy.service;

import com.wy.model.XxlJobInfo;
import com.wy.result.Result;

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
	 * @param jobInfo 定时任务对象
	 * @return 新增成功之后的任务编号jobinfoid
	 */
	Integer add(XxlJobInfo jobInfo);

	/**
	 * 更新定时任务
	 * 
	 * @param jobInfo 定时任务对象
	 * @return 结果,code=1成功,code=0失败
	 */
	Result<?> update(XxlJobInfo jobInfo);

	/**
	 * 删除定时任务
	 * 
	 * @param jobInfoId 定时任务编号
	 * @return 结果,code=1成功,code=0失败
	 */
	Result<?> remove(Integer jobInfoId);

	/**
	 * 开始定时任务,之后将会一直运行
	 * 
	 * @param jobInfoId 定时任务编号
	 * @return 结果,code=1成功,code=0失败
	 */
	Result<?> start(Integer jobInfoId);

	/**
	 * 停止定时任务
	 * 
	 * @param jobInfoId 定时任务编号
	 * @return 结果,code=1成功,code=0失败
	 */
	Result<?> stop(Integer jobInfoId);

	/**
	 * 执行一次定时任务后停止
	 * 
	 * @param jobInfoId 定时任务编号
	 * @param executorParam 执行定时任务的参数,需自定义
	 * @param addressList 执行定时任务的服务地址
	 * @return 结果,code=1成功,code=0失败
	 */
	Result<?> trigger(Integer jobInfoId, String executorParam, String addressList);
}