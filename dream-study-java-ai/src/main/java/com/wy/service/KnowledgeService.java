package com.wy.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;

import com.wy.properties.KnowledgeProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 16:21:00
 */
@Service
@RequiredArgsConstructor
public class KnowledgeService {

	private final VectorStore vectorStore;

	private final ChatClient chatClient;

	private KnowledgeProperties knowledgeProperties;;

	/**
	 * 启动时自动加载知识库并向量化存储
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void init() throws IOException {
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
			vectorStore.add(allDocuments);
			System.out.println("知识库加载完成，总片段数: " + allDocuments.size());
		} else {
			System.out.println("未加载到任何文档");
		}
	}

	/**
	 * 基于本地知识库回答问题
	 */
	public String ask(String question) {
		return chatClient.prompt().user(question).call().content();
	}
}