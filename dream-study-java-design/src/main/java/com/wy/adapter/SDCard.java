package com.wy.adapter;

/**
 * SD卡接口
 *
 * @author 飞花梦影
 * @date 2022-09-15 09:35:25
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface SDCard {

	/**
	 * 读取SD卡方法
	 * 
	 * @return String
	 */
	String read();

	/**
	 * 写入SD卡功能
	 * 
	 * @param msg 消息
	 */
	void write(String msg);
}