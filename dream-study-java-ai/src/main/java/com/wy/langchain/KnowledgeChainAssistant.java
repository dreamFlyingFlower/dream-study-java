package com.wy.langchain;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-05-23 11:30:51
 */
public interface KnowledgeChainAssistant {

	@SystemMessage("""
			你是公司内部知识库助手。回答时请遵循：
			1. 只使用提供的资料，不要编造;
			2. 如资料不足，请明确告诉用户"暂无相关资料";
			3. 回答结尾附上引用的文档来源;
			4.内容请以MarkDown格式出书.
			""")
	String chat(@MemoryId String userId, @UserMessage String question);
}