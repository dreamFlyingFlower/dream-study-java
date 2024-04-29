package com.wy.socketio;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.dream.lang.StrHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-29 09:48:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class SocketIoHelpers {

	/** 暂且把用户&客户端信息存在缓存 */
	public static ConcurrentMap<String, SocketIOClient> connectMap = new ConcurrentHashMap<>();

	@OnEvent(value = "CHANNEL_SYSTEM")
	public void systemDataListener(String receiveMsg) {
		if (StrHelper.isBlank(receiveMsg)) {
			return;
		}
		JSONObject msgObject = (JSONObject) JSON.parse(receiveMsg);
		String userFlag = String.valueOf(msgObject.get("from"));
		String content = String.valueOf(msgObject.get("content"));
		log.info("收到用户 ： {} 推送到系统频道的一条消息 :{}", userFlag, content);
	}

	public void sendToAll(Map<String, Object> msg, String sendChannel) {
		if (connectMap.isEmpty()) {
			return;
		}
		// 给在这个频道的每个客户端发消息
		for (Map.Entry<String, SocketIOClient> entry : connectMap.entrySet()) {
			entry.getValue().sendEvent(sendChannel, msg);
		}
	}

	public void sendToOne(String userFlag, Map<String, Object> msg, String sendChannel) {
		// 拿出某个客户端信息
		SocketIOClient socketClient = getSocketClient(userFlag);
		if (Objects.nonNull(socketClient)) {
			// 单独给他发消息
			socketClient.sendEvent(sendChannel, msg);
		}
	}

	/**
	 * 识别出客户端
	 * 
	 * @param userFlag
	 * @return
	 */
	public SocketIOClient getSocketClient(String userFlag) {
		SocketIOClient client = null;
		if (StrHelper.isNotBlank(userFlag) && !connectMap.isEmpty()) {
			for (String key : connectMap.keySet()) {
				if (userFlag.equals(key)) {
					client = connectMap.get(key);
				}
			}
		}
		return client;
	}
}