package com.wy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.langchain.KnowledgeAssistant;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-05-23 11:29:54
 */
@Configuration
public class LangchainConfig {

	@Bean
	EmbeddingModel embeddingModel() {
		return new AllMiniLmL6V2EmbeddingModel();
	}

	/**
	 * 内存向量化,生产换Milvus / PgVector
	 * 
	 * @return InMemoryEmbeddingStore
	 */
	@Bean
	EmbeddingStore<TextSegment> embeddingStore() {
		return new InMemoryEmbeddingStore<>();
	}

	@Bean
	ContentRetriever contentRetriever(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> store) {
		return EmbeddingStoreContentRetriever.builder()
				.embeddingModel(embeddingModel)
				.embeddingStore(store)
				.maxResults(6)
				.minScore(0.55)
				.build();
	}

	/**
	 * 把 ChatMemoryStore 换成 Redis 持久化
	 * 
	 * @param chatModel
	 * @param retriever
	 * @return
	 */
	@Bean
	KnowledgeAssistant knowledgeAssistant(ChatLanguageModel chatModel, ContentRetriever retriever) {
		return AiServices.builder(KnowledgeAssistant.class)
				.chatLanguageModel(chatModel)
				.contentRetriever(retriever)
				.chatMemoryProvider(uid -> MessageWindowChatMemory.withMaxMessages(30))
				.build();
	}
}