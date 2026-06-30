package com.wy.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 16:18:54
 */
@RestController
@RequestMapping("knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

	private final VectorStore knowledgeVectorStore;

	private final ChatClient knowledgeChatClient;

	/**
	 * 验证知识是否入库
	 * 
	 * @param question
	 */
	@GetMapping("check")
	public void checkKnowledge(String question) {
		// 尝试检索与问题相关的文档
		List<Document> relevantDocs =
				knowledgeVectorStore.similaritySearch(SearchRequest.builder().query(question).topK(3).build());

		System.out.println("检索到 " + relevantDocs.size() + " 个相关文档片段：");
		relevantDocs.forEach(doc -> {
			System.out.println("---");
			System.out.println("内容: " + doc.getText());
			System.out.println("相似度: " + doc.getMetadata().get("distance"));
		});
	}

	/**
	 * 处理聊天请求,直接输出
	 *
	 * @param question 聊天请求
	 * @return 包含AI回复的响应
	 */
	@GetMapping("call")
	public ResponseEntity<String> call(@RequestParam(required = false) String question) {
		try {
			String content = knowledgeChatClient.prompt().user(question).call().content();
			return ResponseEntity.ok(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("处理请求时出错: " + e.getMessage());
		}
	}

	/**
	 * 只使用本地的知识库,把拿到的数据直接拼接
	 * 
	 * @param q
	 * @return
	 */
	@GetMapping("local")
	public String askQuestion(String q) {
		// 1. 检索知识库
		List<Document> docs = knowledgeVectorStore
				.similaritySearch(SearchRequest.builder().query(q).topK(3).similarityThreshold(0.0).build());

		// 2. 如果没找到,直接返回固定信息
		if (docs.isEmpty()) {
			return "【知识库查询结果】未找到相关信息。";
		}

		// 3. 构建返回结果（仅整理，不生成新内容）
		StringBuilder result = new StringBuilder();
		result.append("【知识库查询结果】找到以下相关信息：\n\n");

		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			Double score = (Double) doc.getMetadata().getOrDefault("distance", 0.0);

			result.append("--- 片段 ").append(i + 1);
			result.append("（相关度: ").append(String.format("%.3f", score)).append("）---\n");
			result.append(doc.getText());
			result.append("\n\n");
		}

		// 4. 直接返回整理好的内容，不经过 AI 生成
		return result.toString();
	}
}