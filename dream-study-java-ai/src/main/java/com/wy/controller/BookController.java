package com.wy.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * MCP Server服务提供者
 *
 * @author 飞花梦影
 * @date 2025-04-17 15:13:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@AllArgsConstructor
public class BookController {

	/**
	 * 对应使用McpServerConfig中的ChatClient
	 */
	private final ChatClient chatClient;

	/**
	 * 处理聊天请求，使用AI和MCP工具进行响应
	 *
	 * @param question 聊天请求
	 * @return 包含AI回复的响应
	 */
	@PostMapping
	public ResponseEntity<String> chat(@RequestBody String question) {
		try {
			// 使用流式API调用聊天
			String content = chatClient.prompt().user(question).call().content();
			return ResponseEntity.ok(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("处理请求时出错: " + e.getMessage());
		}
	}
}