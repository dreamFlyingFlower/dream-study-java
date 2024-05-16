package com.wy.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码配置
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:41:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class KaptchaConfig {

	@Bean
	DefaultKaptcha defaultKaptcha() {

		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		properties.setProperty("kaptcha.border.color", "105,179,90");
		properties.setProperty("kaptcha.textproducer.font.color", "black");
		properties.setProperty("kaptcha.image.width", "110");
		properties.setProperty("kaptcha.image.height", "40");
		properties.setProperty("kaptcha.textproducer.char.string",
				"23456789abcdefghkmnpqrstuvwxyzABCDEFGHKMNPRSTUVWXYZ");
		properties.setProperty("kaptcha.textproducer.font.size", "30");
		properties.setProperty("kaptcha.textproducer.char.space", "3");
		properties.setProperty("kaptcha.session.key", "code");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		// properties.setProperty("kaptcha.obscurificator.impl","com.xxx");可以重写实现类
		properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);

		return defaultKaptcha;
	}
}