package com.wy.common;

import java.util.Properties;

/**
 * 一些不经常变的配置文件
 * 
 * @author wanyang 2018年3月8日
 *
 */
public class Constant {

	public static final String URL;
	public static final String FILE_IMAGE;
	public static final String FILE_VEDIO;
	public static final String FILE_AUDIO;
	public static final String FILE_TEXT;
	public static final String FILE_OTHER;
	public static final String FILE_HTTP;

	static {
		try {
			Properties prop = new Properties();
			prop.load(Constant.class.getClassLoader().getResourceAsStream("constant.properties"));
			URL = prop.getProperty("url", "http://192.168.1.1");
			FILE_IMAGE = prop.getProperty("fileImage", "");
			FILE_VEDIO = prop.getProperty("fileVedio", "");
			FILE_AUDIO = prop.getProperty("fileAudio");
			FILE_TEXT = prop.getProperty("fileText", "");
			FILE_OTHER=prop.getProperty("fileOther");
			FILE_HTTP = prop.getProperty("fileHttp", "");
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e.getMessage());
		}
	}
}
