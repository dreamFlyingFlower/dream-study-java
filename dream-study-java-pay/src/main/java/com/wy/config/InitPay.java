package com.wy.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wy.unionpay.util.SDKConfig;

@Component
public class InitPay implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments var) throws Exception {
		SDKConfig.getConfig().loadPropertiesFromSrc();// 银联
	}
}