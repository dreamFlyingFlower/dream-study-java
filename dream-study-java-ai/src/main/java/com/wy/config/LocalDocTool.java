package com.wy.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-30 14:15:22
 */
@Component
@RequiredArgsConstructor
public class LocalDocTool {

	private final VectorStore knowledgeVectorStore;

	/**
	 * 本地知识库检索工具（让模型只能调用本地md，不能联网）
	 */
	@Tool(description = "仅查询本地D:\\person\\repository\\dream-study-notes\\Nginx\\Nginx.md知识库，获取业务资料，无资料则返回空文本，禁止使用网络知识")
	public String searchLocal(@ToolParam(description = "用户提问关键词，用于匹配本地文档") String query) {
		SearchRequest req = SearchRequest.builder().query(query).topK(3).similarityThreshold(0.0).build();
		List<Document> docs = knowledgeVectorStore.similaritySearch(req);
		if (docs.isEmpty()) {
			return "无本地知识库相关内容";
		}
		return docs.stream().map(Document::getText).collect(Collectors.joining("\n"));
	}
}