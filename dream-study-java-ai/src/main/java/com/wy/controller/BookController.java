package com.wy.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * MCP Server服务提供者
 *
 * @author 飞花梦影
 * @date 2025-04-17 15:13:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@AllArgsConstructor
@RequestMapping("chat")
public class BookController {

	/**
	 * 对应使用McpServerConfig中的ChatClient
	 */
	private final ChatClient chatClient;

	/**
	 * 处理聊天请求,直接输出
	 *
	 * @param question 聊天请求
	 * @return 包含AI回复的响应
	 */
	@GetMapping("call")
	public ResponseEntity<String> call(@RequestParam(required = false) String question) {
		try {
			// 使用流式API调用聊天
			String content = chatClient.prompt().user(question).call().content();
			return ResponseEntity.ok(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("处理请求时出错: " + e.getMessage());
		}
	}

	/**
	 * 处理聊天请求,流式输出
	 *
	 * @param question 聊天请求
	 * @return 包含AI回复的响应
	 */
	@GetMapping(value = "stream", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<Flux<String>> stream(@RequestParam(required = false) String question) {
		try {
			// 使用流式API调用聊天
			Flux<String> content = chatClient.prompt().user(question).stream().content();
			return ResponseEntity.ok(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Flux.just("处理请求时出错: " + e.getMessage()));
		}
	}

	/**
	 * 提示词
	 * 
	 * @param sourceLang
	 * @param targetLang
	 * @param text
	 * @return
	 */
	@GetMapping("/translate")
	public String translate(@RequestParam String sourceLang, @RequestParam String targetLang,
			@RequestParam String text) {
		PromptTemplate promptTemplate = new PromptTemplate("""
				请将以下{sourceLang}文本翻译成{targetLang}:
				{text}
				""");
		promptTemplate.add("sourceLang", sourceLang);
		promptTemplate.add("targetLang", targetLang);
		promptTemplate.add("text", text);

		return chatClient.prompt(promptTemplate.create()).call().content();
	}
}