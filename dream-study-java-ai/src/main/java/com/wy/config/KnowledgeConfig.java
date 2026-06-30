package com.wy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 16:44:50
 */
@Configuration
@RequiredArgsConstructor
public class KnowledgeConfig {

	private final LocalDocTool localDocTool;

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.defaultSystem("问答助手").build();
	}

	@Bean("knowledgeChatClient")
	ChatClient knowledgeChatClient(ChatClient.Builder builder, VectorStore knowledgeVectorStore) {
		// // 自定义检索参数：topK=3、相似度0.6
		// SearchRequest searchRequest = SearchRequest.builder()
		// // 返回最多3个片段
		// .topK(3)
		// // 相似度太高,如果本地知识库没有,则会从网上找
		// .similarityThreshold(0.0)
		// .build();
		// var ragAdvisor =
		// QuestionAnswerAdvisor.builder(knowledgeVectorStore).searchRequest(searchRequest).build();
		//
		// // 全局顶层强制约束，双重保险
		// String globalSystem = """
		// 你是仅依托本地文档的问答助手，严格遵守以下规则：
		// 1. 只能使用本次检索返回的doc.md本地文档内容作答；
		// 2. 检索结果为空、无相关内容时，只输出固定文本：【未查询到本地知识库相关内容，无法解答】；
		// 3. 绝对禁止调用你自身训练的互联网知识、常识、外部资料，严禁编造、联想、拓展回答；
		// 4. 回答内容必须完全来自检索文档，不允许添加文档以外任何信息。
		// 5.仅使用本地知识库回答，无相关内容回复【暂无资料】，禁止使用外部网络知识
		// """;
		//
		// return
		// builder.defaultSystem(globalSystem).defaultAdvisors(ragAdvisor).build();
		builder.defaultTools(localDocTool);
		return builder.build();
	}

	// 内存向量库
	// @Bean
	// VectorStore vectorStore(EmbeddingModel embeddingModel) {
	// return SimpleVectorStore.builder(embeddingModel).build();
	// }
	//
	// @Bean
	// ChatClient chatClient(ChatClient.Builder chatClientBuilder, VectorStore
	// vectorStore) {
	// // VectorStore: RAG向量库
	// // QuestionAnswerAdvisor: RAG 增强
	// return chatClientBuilder.build()
	// .mutate()
	// .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
	// .searchRequest(SearchRequest.builder()
	// // 相似度阈值
	// .similarityThreshold(0.8d)
	// // 检索最相关的 3 个片段
	// .topK(6)
	// .build())
	// .build())
	// .build();
	// }
}