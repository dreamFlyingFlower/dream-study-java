package com.wy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用Sharding-jdbc分库分表,暂时只有配置,没有实际代码.支持主流的持久层框架以及第三方连接池,数据库
 * 
 * Sharding-JDBC:数据分片
 * Sharding-Proxy:读写分离
 * Sharding-Sidecar:柔性事务和数据治理功能
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