package com.wy.iterator;

import java.util.ArrayList;
import java.util.List;

import com.wy.entity.Comic;

/**
 * 具体聚合角色
 * 
 * @author 飞花梦影
 * @date 2022-09-16 15:26:18
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ComicAggregateImpl implements ComicAggregate {

	private List<Comic> list = new ArrayList<>();

	@Override
	public void addComic(Comic comic) {
		list.add(comic);
	}

	@Override
	public void removeComic(Comic comic) {
		list.remove(comic);
	}

	// 获取迭代器对象
	@Override
	public ComicIterator getComicIterator() {
		return new ComicIteratorImpl(list);
	}
}