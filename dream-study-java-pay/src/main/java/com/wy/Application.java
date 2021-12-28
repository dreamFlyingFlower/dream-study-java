package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 支付宝接口
 * 
 * 需要将申请的3个证书放在resources下面,具体要到官网进行申请
 * 
 * 优化:
 * <pre>
 * 1.完善alipay
 * 2.去掉jdom
 * 3.完善com.google.zxing

 https://pan.baidu.com/s/1B2_uyrz2uKN1Z_Ivbv7lgw -->
 * </pre>
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