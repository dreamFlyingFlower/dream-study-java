package com.wy.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Cookie实体类
 * 
 * @author 飞花梦影
 * @date 2021-01-07 17:28:31
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlerCookie {

	/**
	 * cookie名称
	 */
	private String name;

	/**
	 * cookie 值
	 */
	private String value;

	/**
	 * 域名
	 */
	private String domain;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 过期时间
	 */
	private Date expire;

	/**
	 * 是否是必须的
	 */
	private boolean isRequired;

	public CrawlerCookie(String name, boolean isRequired) {
		this.name = name;
		this.isRequired = isRequired;
	}

	/**
	 * 校验是否过期
	 *
	 * @return
	 */
	public boolean isExpire() {
		boolean flag = false;
		if (null != expire) {
			flag = expire.getTime() <= (new Date()).getTime();
		}
		return flag;
	}
}