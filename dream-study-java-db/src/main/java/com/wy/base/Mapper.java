package com.wy.base;

import java.io.Serializable;
import java.util.List;

public interface Mapper<T> {

	int save(T t);
	
	int saveBatch(List<T> ts);
	
	int delete(Serializable id);
	
	int deleteBatch(List<T> ts);
	
	int update(T t);
	
	int updateBatch(List<T> ts);
	
	T getById(Serializable id);
	
	/**
	 * 查询所有符合条件的数据,缺一个可以概括所有条件的类
	 * @return
	 */
	List<T> getAll();
	
	/**
	 * 分页查询,还缺一个可以概括所有条件的类
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	List<T> getList(int pageSize,int pageIndex);
	
	/**
	 * 查询符合条件的数据总数,缺一个可以概括条件的类
	 * @return
	 */
	int getCount();
}