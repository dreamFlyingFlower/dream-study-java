package com.wy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.service.BookService;

/**
 * MCP Server配置
 *
 * @author 飞花梦影
 * @date 2025-04-17 15:08:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class McpServerConfig {

	/**
	 * 注册工具回调提供者,将BookService中的@Tool方法暴露为MCP工具
	 *
	 * @param bookService 图书服务
	 * @return 工具回调提供者
	 */
	@Bean
	ToolCallbackProvider toolCallbackProvider(BookService bookService) {
		return MethodToolCallbackProvider.builder().toolObjects(bookService).build();
	}

	/**
	 * 配置ChatClient,注册系统指令和工具函数
	 */
	@Bean
	public ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
		return builder
				.defaultSystem("你是一个图书管理助手，可以帮助用户查询图书信息。你可以根据书名模糊查询、根据作者查询和根据分类查询图书。回复时，请使用简洁友好的语言，并将图书信息整理为易读的格式。")
				// 注册工具方法
				.defaultTools(toolCallbackProvider)
				.build();
	}
}