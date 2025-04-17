package com.wy.repository;

import java.util.List;

import com.wy.entity.BookEntity;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2025-04-17 15:07:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface BookRepository {

	List<BookEntity> findByTitleContaining(String title);

	List<BookEntity> findByAuthor(String author);

	List<BookEntity> findByCategory(String category);
}