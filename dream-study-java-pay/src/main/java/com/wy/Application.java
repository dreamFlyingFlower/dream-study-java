package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 支付宝接口
 * 
 * 需要将申请的3个证书放在resources下面,具体要到官网进行申请
 * 
 * @author 飞花梦影
 * @date 2021-07-15 14:02:28
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}