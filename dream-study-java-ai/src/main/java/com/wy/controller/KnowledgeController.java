package com.wy.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.langchain.KnowledgeAssistant;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-05-23 11:29:16
 */
@RestController
@RequestMapping("knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

	private final KnowledgeAssistant assistant;

	@PostMapping
	public Map<String, String> chat(@RequestHeader("X-User-Id") String userId, @RequestBody Map<String, String> body) {
		String answer = assistant.chat("1", body.get("q"));
		return Map.of("answer", answer);
	}

	@GetMapping
	public Map<String, String> chat(@RequestParam String q) {
		String answer = assistant.chat("1", q);
		return Map.of("answer", answer);
	}
}