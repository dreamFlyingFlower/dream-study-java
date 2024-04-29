package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动类
 * 
 * @author 飞花梦影
 * @date 2021-03-30 11:00:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class WebSocketClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSocketClientApplication.class, args);
	}
}