package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.langchain4j.data.document.DocumentLoader;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

/**
 * LangChain4j
 * 
 * Langchain和spring ai有bean相互覆盖
 * 
 * LangChain4j 的核心其实就七件事：模型、AI Services、记忆、工具、检索增强、流式、结构化输出
 * 
 * <pre>
 * {@link ChatLanguageModel}: 所有对话的起点,给AI消息,AI回复消息.所有模型提供商都只需要实现这一个接口
 *		{@link dev.langchain4j.data.message.SystemMessage}: 设定角色、设定约束
 *		{@link dev.langchain4j.data.message.UserMessage}: 用户输入
 *		{@link AiMessage}: 模型回复
 *		{@link ToolExecutionResultMessage}: 工具执行结果
 *	{@link ChatMemory}: 模型上下文.LLM 本身是无状态的,每一次调用都是独立的.想让它记得上一轮说过什么,就必须把历史消息再塞回去
 *
 *	{@link ChatMemoryStore}: 将历史消息落到 Redis、MySQL 或数据库,这样应用重启、用户切机都不会丢上下文
 *
 * </pre>
 * 
 * LangChain4j 把 RAG 拆成了几个高度解耦的接口:
 * 
 * <pre>
 * {@link DocumentLoader}: 从文件、URL、数据库加载原始文档
 * {@link DocumentSplitter}: 切块(按段落、按句子、按 token)
 * {@link EmbeddingModel}: 把文本变成向量
 * {@link EmbeddingStore}: 向量库(Pinecone、Milvus、PgVector、Elasticsearch…)
 * {@link ContentRetriever}: 把以上组合起来,对外提供"检索"这一件事
 * </pre>
 *
 * @author 飞花梦影
 * @date 2026-05-23 09:45:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}