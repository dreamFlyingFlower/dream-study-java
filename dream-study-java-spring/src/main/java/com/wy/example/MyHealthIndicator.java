package com.wy.example;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * 自定义健康状态检查信息,在actuator/health页面会显示
 * 
 * @author ParadiseWY
 * @date 2020-12-07 23:14:00
 * @git {@link https://github.com/mygodness100}
 */
public class MyHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		// 逻辑处理
		// 代表健康
		Health.up().build();
		return Health.down().withDetail("msg", "异常信息").build();
	}
}