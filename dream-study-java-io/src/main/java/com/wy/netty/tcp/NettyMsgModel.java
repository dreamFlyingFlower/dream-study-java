package com.wy.netty.tcp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:19:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Accessors(chain = true)
public class NettyMsgModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 设备的唯一标识
	 */
	private String imei;

	private String msg;

	private Map<String, Object> bizData;

	public static NettyMsgModel create(String imei, String msg) {
		return new NettyMsgModel().setBizData(new HashMap<>()).setMsg(msg).setImei(imei);
	}
}