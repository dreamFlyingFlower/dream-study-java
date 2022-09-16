package com.wy.iterator;

import com.wy.entity.Comic;

/**
 * 抽象聚合角色接口
 * 
 * @author 飞花梦影
 * @date 2022-09-16 15:23:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface ComicAggregate {

	// 添加学生功能
	void addComic(Comic comic);

	// 删除学生功能
	void removeComic(Comic comic);

	// 获取迭代器对象功能
	ComicIterator getComicIterator();
}