package com.wy.controller;

import java.util.Map;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dream.collection.ListHelper;
import com.dream.collection.MapHelper;

/**
 * 异步测试API
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("openai")
public class OpenAiController {

	@Autowired
	EmbeddingClient embeddingClient;

	@Autowired
	ChatClient chatClient;

	@GetMapping("/ai/embedding")
	public Map<?, ?> embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		EmbeddingResponse embeddingResponse =
				this.embeddingClient.embedForResponse(ListHelper.builder(message).build());
		return MapHelper.of("embedding", embeddingResponse);
	}

	@GetMapping("/ai/chat")
	public String chat(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		Prompt prompt = new Prompt(message);
		return chatClient.call(prompt).getResult().getOutput().getContent();
	}
}