package com.wy.iterator;

import com.wy.entity.Comic;

/**
 * 抽象迭代器角色接口
 * 
 * @author 飞花梦影
 * @date 2022-09-16 15:27:42
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface ComicIterator {

	// 判断是否还有元素
	boolean hasNext();

	// 获取下一个元素
	Comic next();
}