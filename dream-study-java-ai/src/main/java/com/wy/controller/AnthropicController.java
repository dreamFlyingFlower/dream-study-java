//package com.wy.controller;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 使用Anthropic的Cluade进行MCP测试
// *
// * @author 飞花梦影
// * @date 2025-04-17 11:42:05
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@RestController
//@RequestMapping("anthropic")
//public class AnthropicController {
//
//	@Autowired
//	ChatClient chatClient;
//
//	@GetMapping("/ai/chat")
//	public String chat(@RequestParam(defaultValue = "Tell me a joke") String question) {
//		return chatClient.prompt().user(question).call().content();
//	}
//}