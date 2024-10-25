package dream.study.spring.debezium;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Debezium配置文件
 *
 * @author 飞花梦影
 * @date 2024-03-21 13:51:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("dream.debezium")
public class DebeziumProperties {

	/** 是否开启 */
	private boolean enabled = false;

	/** 偏移量文件 */
	private String offsetFileName;

	/** 是否启动时清除偏移量文件 */
	private boolean offsetFileClean = true;

	/** 偏移量提交时间 单位ms */
	private Integer offsetTime = 1;

	/** 读取历史记录文件 */
	private String historyFileName;

	/** 读取的数据库信息 */
	private String dbIp;

	private Integer dbPort;

	private String dbUsername;

	private String dbPassword;

	/** 保证每个数据库读取的instance-name logic-name 不能相同 */
	/** 实例名 */
	private String instanceName;

	/** 逻辑名 */
	private String logicName;

	/** 读取的表 */
	private String includeTable;

	/** 读取的库 */
	private String includeDb;

	/** mysql.cnf 配置的 server-id */
	private Integer serverId;
}