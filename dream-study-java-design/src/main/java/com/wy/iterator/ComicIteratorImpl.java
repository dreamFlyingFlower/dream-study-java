package com.wy.iterator;

import java.util.List;

import com.wy.entity.Comic;

/**
 * 具体迭代器角色类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 15:26:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ComicIteratorImpl implements ComicIterator {

	private List<Comic> list;

	private int position = 0;// 用来记录遍历时的位置

	public ComicIteratorImpl(List<Comic> list) {
		this.list = list;
	}

	@Override
	public boolean hasNext() {
		return position < list.size();
	}

	@Override
	public Comic next() {
		// 从集合中获取指定位置的元素
		Comic currentComic = list.get(position);
		position++;
		return currentComic;
	}
}