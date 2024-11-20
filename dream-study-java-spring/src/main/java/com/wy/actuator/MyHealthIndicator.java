package com.wy.actuator;

import org.springframework.boot.actuate.amqp.RabbitHealthIndicator;
import org.springframework.boot.actuate.cassandra.CassandraDriverHealthIndicator;
import org.springframework.boot.actuate.couchbase.CouchbaseHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.influx.InfluxDbHealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.actuate.jms.JmsHealthIndicator;
import org.springframework.boot.actuate.mail.MailHealthIndicator;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.boot.actuate.neo4j.Neo4jHealthIndicator;
import org.springframework.boot.actuate.redis.RedisHealthIndicator;
import org.springframework.boot.actuate.solr.SolrHealthIndicator;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;

/**
 * 自定义健康状态检查信息,在actuator/health页面会显示
 * 
 * {@link HealthContributorRegistry}:通过该接口收集信息,{@link HealthIndicator}:实现具体的检查逻辑
 * 
 * 内置的数据源健康检查,可参照该类
 * {@link CassandraDriverHealthIndicator},{@link ElasticsearchHealthIndicator},{@link DataSourceHealthIndicator}
 * {@link RabbitHealthIndicator},{@link CouchbaseHealthIndicator},{@link InfluxDbHealthIndicator}
 * {@link Neo4jHealthIndicator},{@link DiskSpaceHealthIndicator},{@link JmsHealthIndicator}
 * {@link SolrHealthIndicator},{@link MailHealthIndicator},{@link MongoHealthIndicator},{@link RedisHealthIndicator}
 * 
 * @author 飞花梦影
 * @date 2020-12-07 23:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		// 逻辑处理
		// 代表健康
		Health.up().build();
		return Health.down().withDetail("msg", "异常信息").build();
	}
}