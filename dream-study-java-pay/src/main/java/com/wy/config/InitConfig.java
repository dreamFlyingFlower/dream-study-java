package com.wy.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wy.util.unionpay.SDKConfig;

@Component
public class InitConfig implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments var) throws Exception {
		SDKConfig.getConfig().loadPropertiesFromSrc();// 银联
	}
}