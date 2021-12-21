package com.wy.base;

import java.util.List;

/**
 * 通用sevice业务接口
 * 
 * @author ParadiseWY
 * @date 2019年6月5日 下午2:59:37
 */
public interface BaseService<T,ID> extends QueryService<T,ID> {

	/**
	 * 新增数据,即使是null也会新增到表中
	 * 
	 * @param t 实体类参数
	 * @return 添加了主键值之后的对象
	 */
	Object insert(T t);

	/**
	 * 新增数据,非null数据才会添加到表中
	 * 
	 * @param t 实体类参数
	 * @return 添加了主键值之后的对象
	 */
	Object insertSelective(T t);

	/**
	 * 批量新增, 不带排序
	 * 
	 * @param ts 实体类参数列表
	 * @return 回显数据
	 */
	Object inserts(List<T> ts);

	/**
	 * 根据表中主键删除单条数据
	 * 
	 * @param id 主键编号
	 * @return 影响条数
	 */
	int delete(ID id);

	/**
	 * 根据表中主键删除批量数据
	 * 
	 * @param ids 主键编号列表
	 * @return 影响条数
	 */
	int deletes(List<ID> ids);

	/**
	 * 删除表中所有数据
	 * 
	 * @return 影响行数
	 */
	int clear();

	/**
	 * 根据主键修改数据,若是数据为null,表中数据也会置为null
	 * 
	 * @param t 需要更新的实体类参数
	 * @return 结果集,int或其他类型
	 */
	int update(T t);

	/**
	 * 根据主键修改数据,只会将参数中的非null数据更新到表中
	 * 
	 * @param t 要修改的数据
	 * @return 结果集,int或其他类型
	 */
	int updateSelective(T t);
}