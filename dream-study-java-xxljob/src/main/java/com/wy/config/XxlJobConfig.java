package com.wy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.common.Constants;
import com.wy.lang.StrTool;
import com.wy.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * xxljob注册配置
 * 
 * 针对多网卡,容器内部署等情况,可借助spring-cloud-commons提供的InetUtils组件灵活定制注册IP
 *
 * 1.引入依赖:org.springframework.cloud->spring-cloud-commons
 * 
 * 2.配置文件,或者容器启动变量 spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
 *
 * 3.获取IP String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
 * 
 * @author 飞花梦影
 * @date 2022-01-04 17:15:43
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
public class XxlJobConfig {

	@Autowired
	private XxlJobProperties xxlJobProperties;

	@Value("${spring.application.name}")
	private String appName;

	@Bean
	public XxlJobSpringExecutor xxlJobExecutor() {
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
		// 设置appName,该值需要先在数据库中创建,否则在操作定时任务时会抛异常
		xxlJobSpringExecutor.setAppname(StrTool.isBlank(xxlJobProperties.getExecutor().getAppname())
				? StrTool.isBlank(appName) ? Constants.XXLJOB_DEFAULT_GROUP_NAME : appName
				: xxlJobProperties.getExecutor().getAppname());
		xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
		xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
		xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
		xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
		return xxlJobSpringExecutor;
	}
}