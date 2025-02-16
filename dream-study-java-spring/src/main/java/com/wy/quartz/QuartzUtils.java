package com.wy.quartz;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import dream.flying.flower.result.ResultException;

/**
 * quartz工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-15 15:34:00
 * @git {@link https://github.com/mygodness100}
 */
public class QuartzUtils {

	/**
	 * 手动执行定时任务
	 * 
	 * @param scheduler 定时任务执行器
	 * @param trigger 定时任务触发器
	 * @throws SchedulerException
	 */
	public static void execute(Scheduler scheduler, Trigger trigger) throws SchedulerException {
		scheduler.scheduleJob(trigger);
	}

	/**
	 * 手动执行定时任务
	 * 
	 * @param scheduler 定时任务执行器
	 * @param jobDetail 定时任务详情
	 * @param trigger 定时任务触发器
	 * @throws SchedulerException
	 */
	public static void execute(Scheduler scheduler, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 手动执行定时任务
	 * 
	 * @param scheduler 定时任务执行器
	 * @param jobId 定时任务编号,数据库中生成的编号
	 * @param jobGroup 定时任务所属组
	 * @throws SchedulerException
	 */
	public static void execute(Scheduler scheduler, String jobId, String jobGroup) throws SchedulerException {
		scheduler.scheduleJob(getTrigger(scheduler, jobId, jobGroup));
	}

	/**
	 * 根据定时任务ID和所属组获得触发器
	 * 
	 * @param scheduler 定时任务执行器
	 * @param jobId 定时任务编号,数据库中生成的编号
	 * @param jobGroup 定时任务所属组
	 * @return Trigger
	 * @throws SchedulerException
	 */
	public static Trigger getTrigger(Scheduler scheduler, String jobId, String jobGroup) throws SchedulerException {
		return scheduler.getTrigger(getTriggerKey(jobId, jobGroup));
	}

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
	 * 构建JobDetail
	 * 
	 * @param clazz 需要执行的job
	 * @param jobGroup 所属组
	 * @return JobDetail
	 */
	public static JobDetail buildJobDetail(Class<? extends Job> clazz, String jobGroup) {
		return JobBuilder.newJob(clazz).withIdentity(clazz.getName(), jobGroup).build();
	}

	/**
	 * 构建Trigger触发器
	 * 
	 * @param clazz 需要执行的job
	 * @param jobGroup 所属组
	 * @param cronScheduleBuilder cron表达式
	 * @return Trigger
	 */
	public static Trigger buildTrigger(Class<? extends Job> clazz, String jobGroup,
			CronScheduleBuilder cronScheduleBuilder) {
		return TriggerBuilder.newTrigger()
				.withIdentity(clazz.getName(), jobGroup)
				.withSchedule(cronScheduleBuilder)
				.build();
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
			// 开启定时任务
			schedulerFactoryBean.getScheduler()
					.scheduleJob(JobBuilder.newJob(QuartzTask.class).build(),
							TriggerBuilder.newTrigger()
									.withSchedule(CronScheduleBuilder.cronSchedule("cron表达式"))
									.build());
			// 判断定时任务是否存在
			schedulerFactoryBean.getScheduler().checkExists(getJobKey(jobId, jobGroup));
			// 移除定时任务
			schedulerFactoryBean.getScheduler().deleteJob(getJobKey(jobId, jobGroup));
			// 暂停定时任务
			schedulerFactoryBean.getScheduler().pauseJob(getJobKey(jobId, jobGroup));
			// 恢复定时任务
			schedulerFactoryBean.getScheduler().resumeJob(getJobKey(jobId, jobGroup));
			// 立即执行定时任务
			schedulerFactoryBean.getScheduler().triggerJob(getJobKey(jobId, jobGroup));
			// 重新按照新的规则执行定时任务,相当于更新定时任务
			schedulerFactoryBean.getScheduler()
					.rescheduleJob(TriggerKey.triggerKey(jobId, jobGroup),
							TriggerBuilder.newTrigger()
									.withSchedule(CronScheduleBuilder.cronSchedule("cron表达式"))
									.build());
		} catch (SchedulerException e) {
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