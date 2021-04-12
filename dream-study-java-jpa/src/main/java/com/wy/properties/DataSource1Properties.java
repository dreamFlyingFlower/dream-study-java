package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据源配置
 *
 * @author 飞花梦影
 * @date 2020-07-07 16:47:17
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class DataSource1Properties {

	private String driverClassName;

	private String url;

	private String username;

	private String password;
}