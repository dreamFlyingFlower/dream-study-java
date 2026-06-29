package com.wy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 16:44:50
 */
@Configuration
public class KnowledgeConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		// 创建 ChatClient 并绑定 QuestionAnswerAdvisor 实现 RAG 增强
		return chatClientBuilder.build()
				.mutate()
				.defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
						.searchRequest(SearchRequest.builder()
								// 相似度阈值
								.similarityThreshold(0.8d)
								// 检索最相关的 3 个片段
								.topK(6)
								.build())
						.build())
				.build();
	}
}