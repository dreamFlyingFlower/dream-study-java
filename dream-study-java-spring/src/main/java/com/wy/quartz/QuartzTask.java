package com.wy.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * quartz的job具体执行方法
 * 
 * @author 飞花梦影
 * @date 2021-01-03 09:44:06
 * @git {@link https://github.com/mygodness100}
 */
public class QuartzTask implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Execute...." + new Date());
		// 获得业务对象,在jobDetail新建的时候设置
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		System.out.println(jobDataMap);
	}
}