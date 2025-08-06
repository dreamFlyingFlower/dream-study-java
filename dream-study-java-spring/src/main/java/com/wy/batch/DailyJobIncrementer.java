package com.wy.batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

/**
 * 自定义Incrementer,防止重复作业
 *
 * @author 飞花梦影
 * @date 2025-08-06 14:10:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DailyJobIncrementer implements JobParametersIncrementer {

	@Override
	public JobParameters getNext(JobParameters parameters) {
		return new JobParametersBuilder(parameters).addLong("run.id", System.currentTimeMillis()).toJobParameters();
	}
}
