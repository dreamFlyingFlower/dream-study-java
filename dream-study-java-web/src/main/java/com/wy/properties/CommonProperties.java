package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用配置类
 * 
 * @author ParadiseWY
 * @date 2020-12-07 13:43:12
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class CommonProperties {

	/** 是否在请求中校验API */
	private boolean validApi = false;
}