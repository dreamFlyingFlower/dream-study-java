package com.wy.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.langchain.KnowledgeChainAssistant;

import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-05-23 11:29:16
 */
@RestController
@RequestMapping("knowledge/langchain")
@RequiredArgsConstructor
public class KnowledgeChainController {

	private final KnowledgeChainAssistant knowledgeChainAssistant;

	@PostMapping
	public Map<String, String> chat(@RequestHeader("X-User-Id") String userId, @RequestBody Map<String, String> body) {
		Result<String> answer = knowledgeChainAssistant.chat("1", body.get("q"));
		return Map.of("answer", answer.content());
	}

	@GetMapping
	public Map<String, Object> chat(@RequestParam String q) {
		Result<String> answer = knowledgeChainAssistant.chat("1", q);
		return Map.of("question", q, "answer", answer.content(), "sources",
				answer.sources().stream().map(s -> s.textSegment().text()).toList());
	}
}