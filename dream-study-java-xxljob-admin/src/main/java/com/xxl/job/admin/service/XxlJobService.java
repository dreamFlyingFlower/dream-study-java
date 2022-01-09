package com.xxl.job.admin.service;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.Date;
import java.util.Map;

/**
 * core job action for xxl-job
 * 
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

	/**
	 * page list
	 *
	 * @param start
	 * @param length
	 * @param jobGroup
	 * @param jobDesc
	 * @param executorHandler
	 * @param author
	 * @return
	 */
	Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc,
			String executorHandler, String author);

	/**
	 * add job
	 *
	 * @param jobInfo
	 * @return
	 */
	ReturnT<String> add(XxlJobInfo jobInfo);

	/**
	 * update job
	 *
	 * @param jobInfo
	 * @return
	 */
	ReturnT<String> update(XxlJobInfo jobInfo);

	/**
	 * remove job
	 * 
	 * @param id
	 * @return
	 */
	ReturnT<String> remove(int id);

	/**
	 * 根据组名和执行器删除任务
	 * 
	 * @param appName 执行器标识
	 * @param executorHandler 执行器名
	 * @return 200->成功,500->失败
	 */
	ReturnT<String> removeByName(String appName, String executorHandler);

	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	ReturnT<String> start(int id);

	/**
	 * 根据组名和执行器开始任务
	 * 
	 * @param appName 执行器标识
	 * @param executorHandler 执行器名
	 * @return 200->成功,500->失败
	 */
	ReturnT<String> startByName(String appName, String executorHandler);

	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	ReturnT<String> stop(int id);

	/**
	 * 根据组名和执行器停止任务
	 * 
	 * @param appName 执行器标识
	 * @param executorHandler 执行器名
	 * @return 200->成功,500->失败
	 */
	ReturnT<String> stopByName(String appName, String executorHandler);

	/**
	 * 根据组名和执行器执行一次任务
	 * 
	 * @param appName 执行器标识
	 * @param executorHandler 执行器名
	 * @param executorParam 执行参数
	 * @param addressList 执行服务地址列表
	 * @return 200->成功,500->失败
	 */
	ReturnT<String> triggerByName(String appName, String executorHandler, String executorParam, String addressList);

	/**
	 * dashboard info
	 *
	 * @return
	 */
	Map<String, Object> dashboardInfo();

	/**
	 * chart info
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate);
}