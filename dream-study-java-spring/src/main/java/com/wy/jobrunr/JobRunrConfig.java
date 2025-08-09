package com.wy.jobrunr;

import javax.sql.DataSource;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.JobActivator;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.mysql.MySqlStorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类似于Quartz的定时异步定时任务,自带Web控制台,可持久化,可内存使用
 *
 * @author 飞花梦影
 * @date 2025-08-09 14:37:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class JobRunrConfig {

	/**
	 * 配置方式使用,或直接使用配置文件
	 * 
	 * @param dataSource
	 * @param jobActivator
	 * @return
	 */
	@Bean
	JobScheduler initJobRunr(StorageProvider storageProvider, JobActivator jobActivator) {
		return JobRunr.configure()
				.useJobActivator(jobActivator)
				.useStorageProvider(storageProvider)
				.useBackgroundJobServer()
				.useDashboard()
				.initialize()
				.getJobScheduler();
	}

	@Bean
	StorageProvider storageProvider(DataSource dataSource, JobMapper jobMapper) {
		// 数据库模式
		MySqlStorageProvider storageProvider = new MySqlStorageProvider(dataSource);
		// 内存模式
		// new InMemoryStorageProvider();
		storageProvider.setJobMapper(jobMapper);
		return storageProvider;
	}
}