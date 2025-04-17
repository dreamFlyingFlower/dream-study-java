package com.wy.service.impl;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.wy.entity.BookEntity;
import com.wy.repository.BookRepository;
import com.wy.service.BookService;

import lombok.AllArgsConstructor;

/**
 * 工具配置方式在需要改造的实现类对需要改造的方法加上@Tool和@ToolParam注解分别标记方法和参数
 *
 * @author 飞花梦影
 * @date 2025-04-17 15:05:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class BookServicImpl implements BookService {

	private final BookRepository bookRepository;

	@Override
	@Tool(name = "findBooksByTitle", description = "根据书名模糊查询图书，支持部分标题匹配")
	public List<BookEntity> findBooksByTitle(@ToolParam(description = "书名关键词") String title) {
		return bookRepository.findByTitleContaining(title);
	}

	@Override
	@Tool(name = "findBooksByAuthor", description = "根据作者精确查询图书")
	public List<BookEntity> findBooksByAuthor(@ToolParam(description = "作者姓名") String author) {
		return bookRepository.findByAuthor(author);
	}

	@Override
	@Tool(name = "findBooksByCategory", description = "根据图书分类精确查询图书")
	public List<BookEntity> findBooksByCategory(@ToolParam(description = "图书分类") String category) {
		return bookRepository.findByCategory(category);
	}
}