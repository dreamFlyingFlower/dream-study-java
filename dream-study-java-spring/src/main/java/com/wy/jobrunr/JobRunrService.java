package com.wy.jobrunr;

import java.util.concurrent.TimeUnit;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.spring.annotations.Recurring;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * {@link Job}:标注了该注解的类将在dashboard中显示,表示一个任务
 * {@link Recurring}:任务需要循环执行.标识了该注解的方法只能包含零个参数或一个JobContext.必须是IOC控制的Bean
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-08-09 14:57:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@AllArgsConstructor
public class JobRunrService {

	final JobScheduler jobScheduler;

	@Job(name = "简单的任务, 没有参数")
	public void execute() {
		log.info("这是一个简单的任务, 没有任何参数");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log.info("任务已完成...");
		}
	}

	@Recurring(id = "job", cron = "0/15 * * * * *")
	@Job(name = "周期性任务", jobFilters = JobRunrFilter.class)
	public void doRecurringJob(JobContext context) {
		System.out.println("周期性执行任务...");
	}
}