package com.wy.shiro.properties;

import com.wy.lang.StrTool;

import lombok.extern.slf4j.Slf4j;

/**
 * 读取Properties的工具类
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:06:04
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Slf4j
public class PropertiesUtil {

	public static LinkProperties propertiesShiro = new LinkProperties();

	/**
	 * 读取properties配置文件信息
	 */
	static {
		String sysName = System.getProperty("sys.name");
		if (StrTool.isBlank(sysName)) {
			sysName = "application.properties";
		} else {
			sysName += ".properties";
		}
		try {
			propertiesShiro
					.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("authentication.properties"));
		} catch (Exception e) {
			log.warn("资源路径中不存在authentication.properties权限文件,忽略读取！");
		}
	}

	/**
	 * 根据key得到value的值
	 */
	public static String getShiroValue(String key) {
		return propertiesShiro.getProperty(key);
	}
}