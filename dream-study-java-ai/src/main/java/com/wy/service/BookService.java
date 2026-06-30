package com.wy.service;

import java.util.List;

import org.springframework.ai.tool.annotation.ToolParam;

import com.wy.entity.BookEntity;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2025-04-17 15:05:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface BookService {

	List<BookEntity> findBooksByTitle(@ToolParam(description = "书名关键词") String title);

	List<BookEntity> findBooksByAuthor(@ToolParam(description = "作者姓名") String author);

	List<BookEntity> findBooksByCategory(@ToolParam(description = "图书分类") String category);
}