package com.wy.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;

import com.wy.properties.KnowledgeProperties;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 16:44:50
 */
@Configuration
@RequiredArgsConstructor
public class VectorStoreConfig {

	private final KnowledgeProperties knowledgeProperties;;

	@Bean
	VectorStore knowledgeVectorStore(EmbeddingModel embeddingModel) throws IOException {
		SimpleVectorStore knowledgeVectorStore = SimpleVectorStore.builder(embeddingModel).build();

		// 加载目录下所有文档写入向量库,数据量太大
		// loadAllDocs(knowledgeVectorStore);

		loadFile(knowledgeVectorStore);

		return knowledgeVectorStore;
	}

	private void loadFile(VectorStore knowledgeVectorStore) {
		// 1. 读取文档
		MarkdownDocumentReaderConfig mdConfig = MarkdownDocumentReaderConfig.builder()
				.withHorizontalRuleCreateDocument(false)
				.withIncludeCodeBlock(true)
				.build();
		FileSystemResource fsResource = new FileSystemResource(knowledgeProperties.getSourceFile());
		MarkdownDocumentReader mdReader = new MarkdownDocumentReader(fsResource, mdConfig);
		List<Document> documents = mdReader.read();

		// 2. 切分文档
		TokenTextSplitter textSplitter = new TokenTextSplitter();
		// TokenTextSplitter.builder()
		// .withChunkSize(800) // 每段最大 800 Token
		// .withMinChunkSizeChars(400) // 每段最小 400 字符
		// .withKeepSeparator(true) // 保留分隔符
		// .build();

		List<Document> chunks = textSplitter.apply(documents);

		// 3. 写入向量数据库（自动完成向量化）
		knowledgeVectorStore.add(chunks);
		System.out.println("成功入库 " + chunks.size() + " 个文档片段。");
	}

	/**
	 * 启动时自动加载知识库并向量化存储
	 * 
	 * @throws IOException
	 */
	private void loadAllDocs(SimpleVectorStore knowledgeVectorStore) throws IOException {
		List<Document> allDocuments = new ArrayList<>();
		Path dirPath = Paths.get(knowledgeProperties.getDirRoot());

		// 检查目录是否存在
		if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
			System.err.println("知识库目录不存在: " + knowledgeProperties.getDirRoot());
			return;
		}

		// 扫描目录下所有 .md 和 .markdown 文件
		try (Stream<Path> paths = Files.walk(dirPath)) {
			List<Path> mdFiles = paths.filter(Files::isRegularFile).filter(p -> {
				String fileName = p.getFileName().toString().toLowerCase();
				return fileName.endsWith(".md") || fileName.endsWith(".markdown");
			}).toList();

			System.out.println("找到 " + mdFiles.size() + " 个 Markdown 文件");

			for (Path mdFile : mdFiles) {
				try {
					// 使用 MarkdownDocumentReader 读取文件
					MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder().build();

					MarkdownDocumentReader reader = new MarkdownDocumentReader(new PathResource(mdFile), config);

					List<Document> documents = reader.read();

					// 为每个 Document 添加来源元数据
					documents.forEach(doc -> {
						doc.getMetadata().put("source_file", mdFile.toString());
						doc.getMetadata().put("file_name", mdFile.getFileName().toString());
					});

					allDocuments.addAll(documents);
					System.out.println("已加载: " + mdFile.getFileName() + " (文档片段数: " + documents.size() + ")");

				} catch (Exception e) {
					System.err.println("读取文件失败: " + mdFile + " - " + e.getMessage());
				}
			}
		}

		// 存入向量数据库
		if (!allDocuments.isEmpty()) {
			knowledgeVectorStore.add(allDocuments);
			System.out.println("知识库加载完成，总片段数: " + allDocuments.size());
		} else {
			System.out.println("未加载到任何文档");
		}
	}
}