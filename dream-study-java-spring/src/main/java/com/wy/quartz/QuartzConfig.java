package com.wy.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Spring整合quartz,需要开启{@link EnableScheduling}
 * 
 * @author 飞花梦影
 * @date 2021-01-03 09:42:31
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class QuartzConfig {

	/**
	 * 创建Job对象
	 */
	@Bean
	JobDetailFactoryBean jobDetailFactoryBean() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		// 关联自己的Job类
		factory.setJobClass(QuartzTask.class);
		// 设置jobdetailmap,业务参数
		factory.setJobDataAsMap(null);
		return factory;
	}

	/**
	 * 创建简单的Trigger
	 */
	@Bean
	SimpleTriggerFactoryBean simpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
		SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
		// 关联JobDetail对象
		factory.setJobDetail(jobDetailFactoryBean.getObject());
		// 执行间隔毫秒数
		factory.setRepeatInterval(2000);
		// 重复次数
		factory.setRepeatCount(5);
		return factory;
	}

	/**
	 * 创建简单Scheduler对象
	 */
	@Bean
	SchedulerFactoryBean simpleSchedulerFactoryBean(SimpleTriggerFactoryBean simpleTriggerFactoryBean) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		// 关联trigger
		factory.setTriggers(simpleTriggerFactoryBean.getObject());
		// 用于quartz集群,QuartzScheduler启动时更新己存在的Job
		// factory.setOverwriteExistingJobs(true);
		// 延长启动
		factory.setStartupDelay(1);
		return factory;
	}

	/**
	 * 创建自定义的定时任务,Cron Trigger
	 */
	@Bean
	CronTriggerFactoryBean cronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(jobDetailFactoryBean.getObject());
		// 设置cron表达式
		factory.setCronExpression("0/2 * * * * ?");
		return factory;
	}

	/**
	 * 创建Scheduler对象
	 */
	@Bean
	SchedulerFactoryBean cronSchedulerFactoryBean(CronTriggerFactoryBean cronTriggerFactoryBean) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		// 关联trigger
		factory.setTriggers(cronTriggerFactoryBean.getObject());
		return factory;
	}
}