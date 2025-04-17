package com.wy.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 使用Anthropic的Cluade进行MCP测试
 *
 * @author 飞花梦影
 * @date 2025-04-17 11:42:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AnthropicController {

	/**
	 * 使用ChatModel和SyncMcpToolCallbackProvider创建一个ChatClient,它将作为我们与模型交互的主要入口点
	 * 
	 * @param chatModel
	 * @param toolCallbackProvider
	 * @return
	 */
	@Bean
	ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider toolCallbackProvider) {
		return ChatClient.builder(chatModel).defaultTools(toolCallbackProvider.getToolCallbacks()).build();
	}

	@Autowired
	ChatClient chatClient;

	@GetMapping("/ai/chat")
	public String chat(@RequestParam(value = "question", defaultValue = "Tell me a joke") String question) {
		return chatClient.prompt().user(question).call().content();
	}
}