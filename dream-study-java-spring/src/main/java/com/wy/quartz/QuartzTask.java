package com.wy.quartz;

import java.util.Date;

import org.quartz.Job;
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
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Execute...." + new Date());
	}
}