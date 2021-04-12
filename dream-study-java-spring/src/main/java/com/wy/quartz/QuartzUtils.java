package com.wy.quartz;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.wy.result.ResultException;

/**
 * quartz工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-15 15:34:00
 * @git {@link https://github.com/mygodness100}
 */
public class QuartzUtils {

	/**
	 * 生成触发器key
	 * 
	 * @param jobId 定时任务编号,数据库中生成的编号
	 * @param jobGroup 定时任务所属组
	 */
	public static TriggerKey getTriggerKey(String jobId, String jobGroup) {
		return TriggerKey.triggerKey(jobId, jobGroup);
	}

	/**
	 * 生成定时器key
	 * 
	 * @param jobId 定时任务编号,数据库中生成的编号
	 * @param jobGroup 定时任务所属组
	 */
	public static JobKey getJobKey(String jobId, String jobGroup) {
		return JobKey.jobKey(jobId, jobGroup);
	}

	/**
	 * 关联相关参数
	 * 
	 * @param jobDetail
	 */
	public static void setParam(JobDetail jobDetail, String jobId, String jobGroup) {
		jobDetail.getJobBuilder().withIdentity(getJobKey(jobId, jobGroup)).build();
	}

	/**
	 * 定时任务相关操作
	 * 
	 * @param schedulerFactoryBean
	 */
	public static void test(SchedulerFactoryBean schedulerFactoryBean, String jobId, String jobGroup) {
		try {
			// 开启定时任务
			schedulerFactoryBean.getScheduler().start();
			// 判断定时任务是否存在
			schedulerFactoryBean.getScheduler().checkExists(getJobKey(jobId, jobGroup));
			// 移除定时任务
			schedulerFactoryBean.getScheduler().deleteJob(getJobKey(jobId, jobGroup));
			// 暂停定时任务
			schedulerFactoryBean.getScheduler().pauseJob(getJobKey(jobId, jobGroup));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置定时任务策略
	 */
	public static CronScheduleBuilder handleMisfirePolicy(CronScheduleBuilder cronScheduleBuilder, int policy) {
		switch (policy) {
			case CronTrigger.MISFIRE_INSTRUCTION_SMART_POLICY:
				return cronScheduleBuilder;
			case CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:
				return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
			case CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING:
				return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
			case CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
				return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
			default:
				return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
		}
	}

	/**
	 * 判断cron表达式是否为正确的表达式
	 *
	 * @param cronExpression cron表达式
	 * @return boolean true有效,false无效
	 */
	public static boolean isValidExpression(String cronExpression) {
		return CronExpression.isValidExpression(cronExpression);
	}

	/**
	 * 根据给定的cron表达式,返回下一次执行时间
	 *
	 * @param cronExpression cron表达式
	 * @return Date 下次cron表达式执行时间
	 */
	public static Date getNextExecution(String cronExpression) {
		if (isValidExpression(cronExpression)) {
			throw new ResultException("定时任务cron表达式无效");
		}
		try {
			CronExpression cron = new CronExpression(cronExpression);
			return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}