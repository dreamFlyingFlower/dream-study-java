package com.wy.proxy;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代理IP实体类
 * 
 * @author 飞花梦影
 * @date 2021-01-07 11:58:03
 * @git {@link https://github.com/mygodness100}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlerProxy implements Serializable {

	private static final long serialVersionUID = 1L;

	private String host;

	private Integer port;

	public String getProxyInfo() {
		return this.host + ":" + port;
	}
}