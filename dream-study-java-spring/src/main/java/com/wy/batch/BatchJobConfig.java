package com.wy.batch;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.model.User;

import dream.flying.flower.result.Result;
import lombok.AllArgsConstructor;

/**
 * 配置批处理作业
 *
 * @author 飞花梦影
 * @date 2025-08-06 13:50:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class BatchJobConfig {

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	/**
	 * 定义Job
	 * 
	 * @param step
	 * @return
	 */
	@Bean
	Job testJob(Step testStep) {
		return jobBuilderFactory.get("testJob")
				// 每日参数
				.incrementer(new DailyJobIncrementer())
				.start(testStep)
				.build();
	}

	/**
	 * 配置Step
	 * 
	 * @param reader
	 * @param processor
	 * @param writer
	 * @return
	 */
	@Bean
	Step testStep(ItemReader<User> reader, ItemProcessor<User, Result<?>> processor, ItemWriter<Result<?>> writer) {
		return stepBuilderFactory.get("testStep")
				// 每1000条提交
				.<User, Result<?>>chunk(1000)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.faultTolerant()
				// 最多跳过100条错误
				.skipLimit(100)
				// 跳过运行时异常
				.skip(RuntimeException.class)
				// .skip(DataValidationException.class)
				.retryLimit(3)
				.retry(DeadlockLoserDataAccessException.class)
				.build();
	}

	/**
	 * 文件读取器:CSV格式
	 * 
	 * @param resource
	 * @return
	 */
	@Bean
	@StepScope
	FlatFileItemReader<User> reader(@Value("#{jobParameters['inputFile']}") Resource resource) {
		return new FlatFileItemReaderBuilder<User>().name("userReader")
				.resource(resource)
				.delimited()
				.names("user_id", "salary", "date", "account")
				.fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {

					{
						setTargetType(User.class);
					}
				})
				.build();
	}

	/**
	 * 数据库比对处理器
	 * 
	 * @param jdbcTemplate
	 * @return
	 */
	@Bean
	ItemProcessor<User, Result<?>> processor(JdbcTemplate jdbcTemplate) {
		return user -> {
			// 查询内部系统记录
			String sql = "SELECT salary FROM user WHERE id = ?";
			BigDecimal internalAmount = jdbcTemplate.queryForObject(sql, BigDecimal.class, user.getUserId());

			// 比对金额差异
			if (internalAmount.compareTo(user.getSalary()) != 0) {
				return Result.ok(user.getUsername() + "AMOUNT_MISMATCH", user.getSalary() + " vs " + internalAmount);
			}
			return null; // 无差异不写入
		};
	}

	/**
	 * 差异报告写入器
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	JdbcBatchItemWriter<Result<?>> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Result<?>>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO recon_results (user_id, error_type, detail) "
						+ "VALUES (:user.id, :errorType, :detail)")
				.dataSource(dataSource)
				.build();
	}

	/**
	 * 解决大数据量内存溢出:JdbcCursorItemReader游标读取
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	JdbcCursorItemReader<User> reader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<User>().name("userReader")
				.dataSource(dataSource)
				.sql("SELECT * FROM user WHERE date = ?")
				.rowMapper(new BeanPropertyRowMapper<>(User.class))
				.preparedStatementSetter(ps -> ps.setDate(1, new Date(Instant.now().toEpochMilli())))
				.fetchSize(5000) // 优化游标大小
				.build();
	}

	/**
	 * 若集成了Prometheus,可加入监控
	 * 
	 * @return
	 */
	// @Bean
	// MeterRegistryCustomizer<MeterRegistry> metrics() {
	//
	// return registry -> {
	// registry.config().commonTags("application", "batch-service");
	// new BatchMetrics().bindTo(registry);
	// };
	// }
}