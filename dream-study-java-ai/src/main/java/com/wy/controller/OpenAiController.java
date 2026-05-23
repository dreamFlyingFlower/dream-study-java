//package com.wy.controller;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.RequiredArgsConstructor;
//
///**
// * AI测试
// * 
// * @author 飞花梦影
// * @date 2021-11-23 21:14:31
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@RestController
//@RequestMapping("openai")
//@RequiredArgsConstructor
//public class OpenAiController {
//
//	private final ChatClient chatClient;
//
//	@GetMapping("/ai/chat")
//	public String chat(@RequestParam(defaultValue = "Tell me a joke") String message) {
//		Prompt prompt = new Prompt(message);
//		return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
//	}
//}