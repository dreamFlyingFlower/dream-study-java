package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * http代理配置
 *
 * @author 飞花梦影
 * @date 2021-12-07 17:09:31
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public class HttpProperties {

	private String proxyHost;

	private int proxyPort;
}