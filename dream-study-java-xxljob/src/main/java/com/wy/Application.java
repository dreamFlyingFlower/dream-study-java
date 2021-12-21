package com.wy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot整合xxl-job
 * 
 * @author 飞花梦影
 * @date 2021-12-21 16:14:00
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@MapperScan("com.wy.mapper")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}