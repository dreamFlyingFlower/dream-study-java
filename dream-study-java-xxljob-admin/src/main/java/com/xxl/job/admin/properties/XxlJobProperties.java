package com.xxl.job.admin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-21 17:31:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ConfigurationProperties(prefix = "xxl.job")
@Getter
@Setter
public class XxlJobProperties {

	private String accessToken;

	private String i18n;

	private Integer logretentiondays = 30;

	private TriggerpoolProperties triggerpool = new TriggerpoolProperties();

	@Getter
	@Setter
	private class TriggerpoolProperties {

		private FastProeprties fast = new FastProeprties();

		private SlowProperties slow = new SlowProperties();
	}

	@Getter
	@Setter
	private class FastProeprties {

		private Integer max = 200;
	}

	@Getter
	@Setter
	private class SlowProperties {

		private Integer max = 200;

	}
}