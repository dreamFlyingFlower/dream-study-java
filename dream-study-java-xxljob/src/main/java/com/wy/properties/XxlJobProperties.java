package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.wy.enums.ExecutorRouteStrategyEnum;
import com.wy.enums.MisfireStrategyEnum;
import com.wy.enums.ScheduleTypeEnum;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * xxljob参数
 *
 * @author 飞花梦影
 * @date 2021-12-21 17:31:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ConfigurationProperties(prefix = "xxl.job")
@Configuration
@Getter
@Setter
public class XxlJobProperties {

	/** http登录xxljob-admin的用户名和密码 */
	private String username = "admin";

	private String password = "123456";

	private String accessToken;

	/** admin管理后台地址 */
	private String adminAddresses = "http://127.0.0.1:8080/xxl-job-admin";

	private Executor executor = new Executor();

	private JobInfo jobInfo = new JobInfo();

	@Getter
	@Setter
	public class Executor {

		private String appname;

		private String address;

		private String ip;

		private int port = 9999;

		private String logPath = "logs/xxl-job/jobhandler";

		/** 日志存储天数 */
		private int logRetentionDays = 30;
	}

	@Getter
	@Setter
	public class JobInfo {

		/** 调度类型 */
		private ScheduleTypeEnum scheduleType = ScheduleTypeEnum.CRON;

		/** 运行模式 */
		private GlueTypeEnum glueType = GlueTypeEnum.BEAN;

		/** 路由策略 */
		private ExecutorRouteStrategyEnum executorRouteStrategy = ExecutorRouteStrategyEnum.FIRST;

		/** 调度过期策略 */
		private MisfireStrategyEnum misfireStrategy = MisfireStrategyEnum.DO_NOTHING;

		/** 阻塞处理策略 */
		private ExecutorBlockStrategyEnum executorBlockStrategy = ExecutorBlockStrategyEnum.SERIAL_EXECUTION;
	}
}