package com.wy.proxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * proxy 校验
 * 
 * @author 飞花梦影
 * @date 2021-01-07 13:56:34
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyValidate {

	/**
	 * 主机
	 */
	private String host;

	/**
	 * 端口号
	 */
	private int port;

	/**
	 * 错误码
	 */
	private int returnCode;

	/**
	 * 耗时
	 */
	private int duration;

	/**
	 * 错误信息
	 */
	private String error;

	public ProxyValidate(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 获取代理对象
	 * 
	 * @return
	 */
	public CrawlerProxy getProxy() {
		return new CrawlerProxy(host, port);
	}
}