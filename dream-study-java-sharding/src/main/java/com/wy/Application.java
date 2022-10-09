package com.wy;

import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.core.strategy.route.complex.ComplexShardingStrategy;
import org.apache.shardingsphere.core.strategy.route.hint.HintShardingStrategy;
import org.apache.shardingsphere.core.strategy.route.inline.InlineShardingStrategy;
import org.apache.shardingsphere.core.strategy.route.none.NoneShardingStrategy;
import org.apache.shardingsphere.core.strategy.route.standard.StandardShardingStrategy;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用Sharding-jdbc分库分表,暂时只有配置,没有实际代码.支持主流的持久层框架以及第三方连接池,数据库
 * 
 * Sharding-JDBC:数据分片;Sharding-Proxy:读写分离;Sharding-Sidecar:柔性事务和数据治理功能
 * 
 * <pre>
 * {@link ShardingDataSourceFactory}:主要入口,分库分表+读写分离
 * {@link MasterSlaveDataSourceFactory}:独立的读写分离
 * {@link MasterSlaveRuleConfiguration}:读写分离核心配置信息入口
 * {@link TableRuleConfiguration}:表分片规则
 * {@link ShardingRuleConfiguration}:分库分表+读写分离核心配置信息,包含多个MasterSlaveRuleConfiguration和多个TableRuleConfiguration
 * {@link ShardingStrategyConfiguration}:分库分表策略配置
 * ->{@link StandardShardingStrategyConfiguration}:
 * ->{@link InlineShardingStrategyConfiguration}:
 * ->{@link ComplexShardingStrategyConfiguration}:
 * ->{@link NoneShardingStrategyConfiguration}:
 * ->{@link HintShardingStrategyConfiguration}:
 * </pre>
 * 
 * https://blog.csdn.net/shijiemozujiejie/article/details/80786231
 * 
 * 不停机分库分表数据迁移
 * 
 * <pre>
 * 1.利用MySQL+Canal做增量数据同步,利用分库分表中间件,将数据路由到对应的新表中
 * 2.利用分库分表中间件,全量数据导入到对应的新表中
 * 3.通过单表数据和分库分表数据两两比较,更新不匹配的数据到新表中
 * 4.数据稳定后,将单表的配置切换到分库分表配置上
 * </pre>
 * 
 * 广播表:有些表没必要做分片,如字典表,省份信息等,而且这种表可能需要与海量数据的表进行关联查询.广播表会在不同的数据节点上进行存储,存储的表结构和数据完全相同
 * 
 * 分片算法:ShardingAlgorithm,由于分片算法和业务紧密相关,因此并未提供内置分片算法,而是通过分片策略将各种场景提炼出来,并提供接口让开发者自行实现分片算法
 * 
 * <pre>
 * 精确分片算法:{@link PreciseShardingAlgorithm},用于处理使用单一键作为分片键的=与IN进行分片的场景
 * 范围分片算法:{@link RangeShardingAlgorithm},用于处理使用单一键作为分片键的BETWEEN AND、>、<、>=、<=进行分片的场景
 * 复合分片算法:{@link ComplexKeysShardingAlgorithm},用于处理使用多键作为分片键进行分片的场景,多个分片键的逻辑较复杂,需要应用开发者自行处理其中的复杂度
 * Hint分片算法:{@link HintShardingAlgorithm},用于处理使用Hint行分片的场景.对于分片字段非SQL决定,而由其他外置条件决定的场景,可使用SQL Hint灵活的注入分片字段.
 * 		如:内部系统,按照员工登录主键分库,而数据库中并无此字段.SQL Hint支持通过Java API和SQL注释两种方式使用
 * </pre>
 * 
 * 分片策略:ShardingStrategy,包含分片键和分片算法,真正可用于分片操作的是分片键 + 分片算法,也就是分片策略,目前提供5种分片策略
 * 
 * <pre>
 * 标准分片策略:{@link StandardShardingStrategy},只支持单分片键,提供对SQL语句中的=, >, <, >=, <=, IN和BETWEEN AND的分片操作支持,
 * 		提供{@link PreciseShardingAlgorithm}和{@link RangeShardingAlgorithm}两个分片算法.PreciseShardingAlgorithm必选,RangeShardingAlgorithm可选.
 * 		如果SQL中使用了范围操作,但没有配置RangeShardingAlgorithm,会采用全库路由扫描,效率低
 * 	复合分片策略:{@link ComplexShardingStrategy},支持多分片键,提供对SQL语句中的=, >, <, >=, <=, IN和BETWEEN AND的分片操作支持.
 * 		由于多分片键之间的关系复杂,因此并未进行过多的封装,而是直接将分片键值组合以及分片操作符透传至分片算法,完全由应用开发者实现,提供最大的灵活度
 * 行表达式分片策略:{@link InlineShardingStrategy},只支持单分片键,使用Groovy的表达式,提供对SQL语句中的=和IN的分片操作支持.
 * 		对于简单的分片算法,可以通过简单的配置使用,从而避免繁琐的Java代码开发.如: t_user_$->{u_id % 8} 表示t_user表根据u_id模8,而分成8张表,表名称为t_user_0到t_user_7
 * Hint分片策略:{@link HintShardingStrategy},通过Hint指定分片值而非从SQL中提取分片值的方式进行分片的策略,强制分片路由
 * 不分片策略:{@link NoneShardingStrategy}
 * </pre>
 * 
 * 分片策略配置:
 * 
 * <pre>
 * 对于分片策略存有数据源分片策略和表分片策略两种维度,两种策略的API完全相同
 * 数据源分片策略:用于配置数据被分配的目标数据源
 * 表分片策略:用于配置数据被分配的目标表,由于表存在与数据源内,所以表分片策略是依赖数据源分片策略结果的
 * </pre>
 * 
 * 不支持项
 * 
 * <pre>
 * 不支持case when,having,union,union all
 * 支持分页子查询,但其他子查询有限支持,无论嵌套多少层,只能解析至第一个包含数据表的子查询,一旦在下层嵌套中再次找到包含数据表的子查询,将直接抛异常
 * 子查询中包含聚合函数目前无法支持
 * 不支持包含schema的SQL.因为ShardingSphere的理念是像使用一个数据源一样使用多数据源,因此对SQL的访问都是在同一个逻辑schema之上
 * 当分片键处于运算表达式或函数中的SQL时,将采用全路由的形式获取结果
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-07-13 09:14:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@MapperScan("com.wy.mapper")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}