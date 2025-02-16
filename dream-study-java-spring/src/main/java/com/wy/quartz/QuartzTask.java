package com.wy.quartz;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * quartz的job具体执行方法,若有dataJobMap属性,可继承{@link QuartzJobBean}
 * 
 * {@link DisallowConcurrentExecution}:标记类为不能同时执行多个实例的类.其中实例基于{@link JobDetail}定义,即基于{@link JobKey}
 * 
 * @author 飞花梦影
 * @date 2021-01-03 09:44:06
 * @git {@link https://github.com/mygodness100}
 */
@Component
@DisallowConcurrentExecution
public class QuartzTask implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时任务开始Execute...." + new Date());
		// 获得业务对象,在jobDetail新建的时候设置
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		System.out.println(jobDataMap);
	}
}