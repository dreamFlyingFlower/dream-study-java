package com.wy.netty.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 回复消息
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response {

	/**
	 * 模块号
	 */
	private short module;

	/**
	 * 命令号
	 */
	private short cmd;

	/**
	 * 结果码
	 */
	@Builder.Default
	private int stateCode = ResultCode.SUCCESS;

	/**
	 * 数据
	 */
	private byte[] data;

	public Response(Request message) {
		this.module = message.getModule();
		this.cmd = message.getCmd();
	}

	public Response(short module, short cmd, byte[] data) {
		this.module = module;
		this.cmd = cmd;
		this.data = data;
	}
}