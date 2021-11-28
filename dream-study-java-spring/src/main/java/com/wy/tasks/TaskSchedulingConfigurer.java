package com.wy.tasks;

import java.util.Date;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 实现{@link SchedulingConfigurer}可以动态修改定时任务表达式,同样需要开启{@link EnableScheduling}
 * 
 * @author 飞花梦影
 * @date 2020-12-28 22:20:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Async
@Component
public class TaskSchedulingConfigurer implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addCronTask(() -> {
			// 定时任务的业务逻辑
		}, "cron表达式1");

		// 可添加多个
		taskRegistrar.addCronTask(() -> {

		}, "cron表达式2");

		// 效果同上
		taskRegistrar.addTriggerTask(() -> {

		}, new Trigger() {

			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				CronTrigger cronTrigger = new CronTrigger("cron表达式");
				Date nextExecutionTime = cronTrigger.nextExecutionTime(triggerContext);
				return nextExecutionTime;
			}
		});
	}
}