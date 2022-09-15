package com.wy.adapter;

/**
 * TFCard接口
 *
 * @author 飞花梦影
 * @date 2022-09-15 09:38:19
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface TFCard {

	/**
	 * 读取TF卡方法
	 * 
	 * @return String
	 */
	String readTF();

	/**
	 * 写入TF卡功能
	 * 
	 * @param msg 消息
	 */
	void writeTF(String msg);
}