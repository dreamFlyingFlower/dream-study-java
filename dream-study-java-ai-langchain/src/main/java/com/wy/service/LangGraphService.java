package com.wy.service;

import java.util.List;
import java.util.Map;

import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;

import com.wy.langgraph.ChatGraphState;

import dev.langchain4j.data.message.ChatMessage;
import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 15:16:41
 */
@Service
@RequiredArgsConstructor
public class LangGraphService {

	private final CompiledGraph<ChatGraphState> chatGraph;

	public void runAgent() throws Exception {
		// 1. 准备初始数据
		Map<String, Object> initData = Map.of(ChatGraphState.MESSAGES_KEY, List.of("Start message"));

		// 2. 调用图，传入初始状态
		ChatGraphState finalState = chatGraph.invoke(initData).get();

		// 3. 获取结果并打印
		List<ChatMessage> messages = finalState.getMessages();
		System.out.println("最终消息列表: " + messages);
		// 输出: 最终消息列表: [Start message, Hello from Async CustomizeNode!]
	}
}