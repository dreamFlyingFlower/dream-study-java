package com.wy.batch;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * 启动作业
 *
 * @author 飞花梦影
 * @date 2025-08-06 13:55:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class BatchApplication implements CommandLineRunner {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job bankReconciliationJob;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		JobParameters params = new JobParametersBuilder().addString("inputFile", "classpath:data/trans-20230520.csv")
				.addDate("runDate", new Date())
				.toJobParameters();

		jobLauncher.run(bankReconciliationJob, params);
	}
}