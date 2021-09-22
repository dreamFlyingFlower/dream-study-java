package com.wy.netty.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 消息对象
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Request {

	/**
	 * 模块号
	 */
	private short module;

	/**
	 * 命令号
	 */
	private short cmd;

	/**
	 * 数据
	 */
	private byte[] data;

	public static Request valueOf(short module, short cmd, byte[] data) {
		Request request = new Request();
		request.setModule(module);
		request.setCmd(cmd);
		request.setData(data);
		return request;
	}
}