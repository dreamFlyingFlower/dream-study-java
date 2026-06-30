package com.wy.langgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;

/**
 * 将业务逻辑(如调用LLM、查询数据库或调用工具)封装为节点,并注册为Spring的@Component,实现NodeAction接口
 * 
 * {@link NodeAction}: 同步节点 {@link AsyncNodeAction}: 异步节点
 *
 * @author 飞花梦影
 * @date 2026-06-29 14:44:17
 */
@Component
@RequiredArgsConstructor
public class CustomizeNode implements AsyncNodeAction<ChatGraphState> {

	private final ChatLanguageModel chatModel;

	@Override
	public CompletableFuture<Map<String, Object>> apply(ChatGraphState state) {
		// 1. 获取当前状态中的消息列表
		List<ChatMessage> messages = state.getMessages();
		System.out.println("CustomizeNode 异步执行，当前消息: " + messages);

		// 2. 模拟异步操作（例如调用外部API、数据库查询等）
		return CompletableFuture.supplyAsync(() -> {
			// 模拟耗时操作
			try {
				Thread.sleep(1000); // 模拟1秒延迟
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("异步操作被中断", e);
			}

			// 3. 执行业务逻辑，向列表中添加一条新消息
			AiMessage aiMsg = chatModel.generate(messages).content();

			List<ChatMessage> newMsgList = new ArrayList<>(messages);
			newMsgList.add(aiMsg);

			// 4. 返回状态更新 Map
			return Map.of(ChatGraphState.MESSAGES_KEY, newMsgList);
		});
	}
}