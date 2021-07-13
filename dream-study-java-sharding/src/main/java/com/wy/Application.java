package com.wy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用Sharding-jdbc分库分表,暂时只有配置,没有实际代码
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