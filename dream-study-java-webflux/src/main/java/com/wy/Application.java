package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WebFlux:用于构建基于Reactive技术栈之上的Web应用程序,基于Reactive Stream API,运行于非阻塞服务器上
 * 
 * WebFlux在请求的耗时上并没有太大改善,仅需少量固定数量线程和较少内存即可实现扩展
 * 
 * @author 飞花梦影
 * @date 2021-09-30 13:26:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}