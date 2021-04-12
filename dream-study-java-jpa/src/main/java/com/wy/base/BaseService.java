package com.wy.base;

import java.util.List;

/**
 * 修改类通用业务接口 TODO
 * 
 * @author 飞花梦影
 * @date 2021-01-06 22:42:47
 * @git {@link https://github.com/mygodness100}
 */
public interface BaseService<T, ID> extends QueryService<T, ID> {

	/**
	 * 根据实体类中主键删除单条数据
	 * 
	 * @param t 实体类
	 */
	void delete(T t);

	/**
	 * 清空表中所有数据
	 */
	void deleteAll();

	/**
	 * 根据表中主键删除单条数据
	 * 
	 * @param id 主键编号
	 */
	void deleteById(ID id);

	/**
	 * 根据表中主键删除批量数据,不推荐使用,最好组装成实体类集合进行批量删除
	 * 
	 * @param ids 主键编号列表
	 */
	void deleteByIds(List<ID> ids);

	/**
	 * 利用主键批量删除数据
	 * 
	 * @param entities 实体类,主键必须非null
	 */
	void deletes(List<T> entities);

	/**
	 * 单条新增或修改
	 * 
	 * @param model 实体类参数
	 * @return 回显数据
	 */
	Object saveOrUpdate(T model);

	/**
	 * 批量新增, 不带排序
	 * 
	 * @param ts 实体类参数列表
	 * @return 回显数据
	 */
	Object saveOrUpdates(List<T> ts);

	/**
	 * 全量字段更新表中数据,只有带readonly注解是字段不更新
	 * 
	 * @param model 需要更新的实体类参数
	 * @return 结果集,int或其他类型
	 */
	Object update(T model);
}