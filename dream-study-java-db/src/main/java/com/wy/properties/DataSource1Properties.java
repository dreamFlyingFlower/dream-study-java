package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡口数据源配置
 * 
 * @author ParadiseWY
 * @date 2020年7月7日 下午4:47:52
 */
@Getter
@Setter
public class DataSource1Properties {

	private String driverClassName;

	private String url;

	private String username;

	private String password;
}