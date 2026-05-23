package com.wy.langchain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 项目启动时,读取指定目录,自建索引
 *
 * @author 飞花梦影
 * @date 2026-05-23 11:31:16
 */
@Component
@RequiredArgsConstructor
public class KnowledgeIndexer {

	private final EmbeddingModel embeddingModel;

	private final EmbeddingStore<TextSegment> store;

	@PostConstruct
	public void indexAtStartup() throws IOException {
		// 按段落/token切分md
		DocumentSplitter splitter = DocumentSplitters.recursive(500, 80);
		try (Stream<Path> files = Files.walk(Path.of("D:\\person\\repository\\dream-study-notes"))) {
			files.filter(p -> p.toString().endsWith(".md")).forEach(p -> {
				// 读pdf
				// Document doc = FileSystemDocumentLoader.loadDocument(p, new
				// ApachePdfBoxDocumentParser());
				Document doc = FileSystemDocumentLoader.loadDocument(p, new TextDocumentParser());
				// 切分
				List<TextSegment> segs = splitter.split(doc);
				List<Embedding> embs = embeddingModel.embedAll(segs).content();
				store.addAll(embs, segs);
			});
		}
	}
}