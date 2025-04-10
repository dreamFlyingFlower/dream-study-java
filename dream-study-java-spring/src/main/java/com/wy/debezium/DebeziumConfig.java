package com.wy.debezium;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import dream.flying.flower.io.file.FileHelper;
import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Debezium是一个用来捕获数据库数据变更的分布式服务,应用可以看到这些数据变更,以及处理他们
 * 
 * Debezium以更改事件流的形式记录每张表的行级变更,然后应用可以以事件流产生的顺序读取事件流变更记录
 * 
 * 目前支持的Source Connectors是Mysql,MongoDB,PostgresSQL、Oracle、SQL
 * Server、Db2、Cassamdra、Vitesss
 *
 * @author 飞花梦影
 * @date 2024-03-21 13:10:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@AllArgsConstructor
public class DebeziumConfig {

	private final ChangeEventHandler changeEventHandler;

	private final DebeziumProperties debeziumProperties;

	@Bean
	void cleanFile() {
		if (debeziumProperties.isOffsetFileClean()
				&& null != FileHelper.checkFile(debeziumProperties.getOffsetFileName())) {
			try {
				FileHelper.delete(debeziumProperties.getOffsetFileName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Debezium 配置.
	 *
	 * @return configuration
	 */
	@Bean
	io.debezium.config.Configuration debeziumConfig() {
		return io.debezium.config.Configuration.create()
				// 连接器的Java类名称
				.with("connector.class", MySqlConnector.class.getName())
				// 偏移量持久化,用来容错 默认值
				.with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
				// 偏移量持久化文件路径,默认/tmp/offsets.dat.如果路径配置不正确可能导致无法存储偏移量,可能会导致重复消费变更
				// 如果连接器重新启动,它将使用最后记录的偏移量来知道它应该恢复读取源信息中的哪个位置
				.with("offset.storage.file.filename", debeziumProperties.getOffsetFileName())
				// 捕获偏移量的周期,尝试提交偏移量的时间间隔,默认值为1分钟
				.with("offset.flush.interval.ms", debeziumProperties.getOffsetTime())
				// 监听连接器的唯一名称
				.with("name", debeziumProperties.getInstanceName())
				// 数据库的hostname
				.with("database.hostname", debeziumProperties.getDbIp())
				// 端口
				.with("database.port", debeziumProperties.getDbPort())
				// 用户名
				.with("database.user", debeziumProperties.getDbUsername())
				// 密码
				.with("database.password", debeziumProperties.getDbPassword())
				// 要从中捕获更改的数据库的名称
				.with("database.dbname", debeziumProperties.getDbName())
				// 要捕获变化包含的数据库列表
				.with("database.include.list", debeziumProperties.getIncludeDb())
				// 应捕获其更改的所有表的列表
				.with("table.include.list", debeziumProperties.getIncludeTable())
				// 是否包含数据库表结构层面的变更,建议使用默认值true
				.with("include.schema.changes", debeziumProperties.getIncludeSchemaChange())
				// mysql.cnf 配置的 server-id
				.with("database.server.id", debeziumProperties.getServerId())
				// MySQL服务器或集群的逻辑名称,形成命名空间,用于连接器写入的所有 Kafka 主题的名称、Kafka Connect 架构名称以及 Avro
				// 转换器时对应的Avro 架构的命名空间用来
				.with("database.server.name", debeziumProperties.getLogicName())
				// 负责数据库历史变更记录持久化Java类名
				.with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
				// 历史变更记录存储位置,存储DDL
				.with("database.history.file.filename", debeziumProperties.getHistoryFileName())
				.build();
	}

	/**
	 * 实例化sql server 实时同步服务类,执行任务
	 *
	 * @param configuration
	 * @return SqlServerTimelyExecutor
	 */
	@Bean
	SqlServerTimelyExecutor sqlServerTimelyExecutor(io.debezium.config.Configuration configuration) {
		SqlServerTimelyExecutor sqlServerTimelyExecutor = new SqlServerTimelyExecutor();
		DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine =
				DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
						.using(configuration.asProperties())
						.notifying(changeEventHandler::handlePayload)
						.build();
		sqlServerTimelyExecutor.setDebeziumEngine(debeziumEngine);
		return sqlServerTimelyExecutor;
	}

	/**
	 * 同步执行服务类
	 *
	 * @author 飞花梦影
	 * @date 2024-03-21 14:15:08
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	@Data
	@Slf4j
	public static class SqlServerTimelyExecutor implements InitializingBean, SmartLifecycle {

		private final ExecutorService executor = ThreadPoolEnum.INSTANCE.getInstance();

		private DebeziumEngine<?> debeziumEngine;

		@Override
		public void start() {
			log.warn(ThreadPoolEnum.SQL_SERVER_LISTENER_POOL + "线程池开始执行 debeziumEngine 实时监听任务!");
			executor.execute(debeziumEngine);
		}

		@SneakyThrows
		@Override
		public void stop() {
			log.warn("debeziumEngine 监听实例关闭!");
			debeziumEngine.close();
			Thread.sleep(2000);
			log.warn(ThreadPoolEnum.SQL_SERVER_LISTENER_POOL + "线程池关闭!");
			executor.shutdown();
		}

		@Override
		public boolean isRunning() {
			return false;
		}

		@Override
		public void afterPropertiesSet() {
			Assert.notNull(debeziumEngine, "DebeZiumEngine 不能为空!");
		}

		public enum ThreadPoolEnum {

			/**
			 * 实例
			 */
			INSTANCE;

			public static final String SQL_SERVER_LISTENER_POOL = "sql-server-listener-pool";

			/**
			 * 线程池单例
			 */
			private final ExecutorService es;

			/**
			 * 枚举 (构造器默认为私有）
			 */
			ThreadPoolEnum() {
				final ThreadFactory threadFactory =
						new ThreadFactoryBuilder().setNameFormat(SQL_SERVER_LISTENER_POOL + "-%d").build();
				es = new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(256), threadFactory,
						new ThreadPoolExecutor.DiscardPolicy());
			}

			/**
			 * 公有方法
			 *
			 * @return ExecutorService
			 */
			public ExecutorService getInstance() {
				return es;
			}
		}
	}
}