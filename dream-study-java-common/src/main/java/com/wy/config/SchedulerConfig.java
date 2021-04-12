package com.wy.config;

import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务
 * 可不实现SchedulerFactoryBeanCustomizer接口,直接写配置即可
 * @author wanyang 2018年7月19日
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {

	@Override
	public void customize(SchedulerFactoryBean schedulerFactoryBean) {
		
	}
}