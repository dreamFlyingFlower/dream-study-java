package com.wy.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.langchain.KnowledgeChainAssistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
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

	/**
	 * RAG（检索增强生成）
	 * 
	 * @param embeddingModel
	 * @param store
	 * @return
	 */
	@Bean
	ContentRetriever contentRetriever(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> store) {

		load(embeddingModel, store);

		return EmbeddingStoreContentRetriever.builder()
				.embeddingModel(embeddingModel)
				.embeddingStore(store)
				// 检索最相关的5个片段
				.maxResults(5)
				// 相似度阈值,低于此值的结果将被过滤
				.minScore(0.0)
				.build();
	}

	private void load(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> store) {

		List<Document> documents = FileSystemDocumentLoader.loadDocuments("D:\\person\\repository\\dream-study-notes\\Nginx");
		DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(1000, 200);
		EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
				.documentSplitter(splitter)
				// 为每个文本片段附加上源文件名，提升检索质量 [citation:6]
				.textSegmentTransformer(segment -> TextSegment
						.from(segment.metadata().getString("file_name") + "\n" + segment.text(), segment.metadata()))
				.embeddingModel(embeddingModel)
				.embeddingStore(store)
				.build();
		ingestor.ingest(documents);

		// DocumentSplitter splitter = DocumentSplitters.recursive(500, 80);
		// 按段落/token切分md
		// try (Stream<Path> files =
		// Files.walk(Path.of("D:\\person\\repository\\dream-study-notes"))) {
		// files.filter(p -> p.toString().endsWith(".md")).forEach(p -> {
		// // 读pdf
		// // Document doc = FileSystemDocumentLoader.loadDocument(p, new
		// // ApachePdfBoxDocumentParser());
		// Document doc = FileSystemDocumentLoader.loadDocument(p, new
		// TextDocumentParser());
		// // 切分
		// List<TextSegment> segs = splitter.split(doc);
		// List<Embedding> embs = embeddingModel.embedAll(segs).content();
		// store.addAll(embs, segs);
		// });
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * 把 ChatMemoryStore 换成 Redis 持久化
	 * 
	 * @param chatModel
	 * @param retriever
	 * @return
	 */
	@Bean
	KnowledgeChainAssistant knowledgeChainAssistant(ChatLanguageModel chatModel, ContentRetriever contentRetriever) {
		return AiServices.builder(KnowledgeChainAssistant.class)
				.chatLanguageModel(chatModel)
				.contentRetriever(contentRetriever)
				.chatMemoryProvider(uid -> MessageWindowChatMemory.withMaxMessages(30))
				.build();
	}
}