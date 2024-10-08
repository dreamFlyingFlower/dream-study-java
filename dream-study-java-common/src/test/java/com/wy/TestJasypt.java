package com.wy;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 使用jasypt代码加密,需要在配置文件中配置jasypt.encryptor.password
 *
 * @author 飞花梦影
 * @date 2024-10-08 16:51:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootTest
class TestJasypt {

	@Autowired
	private StringEncryptor stringEncryptor;

	@Test
	void contextLoads() {
		System.out.println(stringEncryptor.encrypt("yang_wan"));
	}

}