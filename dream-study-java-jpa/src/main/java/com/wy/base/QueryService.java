package com.wy.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.wy.result.Result;

/**
 * 查询类通用基础数据接口 TODO
 * 
 * @author 飞花梦影
 * @date 2021-01-06 22:51:49
 * @git {@link https://github.com/mygodness100}
 */
public interface QueryService<T, ID> {

	void count(Specification<T> specification);

	void count();

	/**
	 * 根据表中主键查询单条数据详情
	 * 
	 * @param id 主键编号
	 * @return 单实体类结果集
	 */
	T getById(ID id);

	/**
	 * 根据表中主键查询多条数据详情
	 * 
	 * @param ids 主键编号集合
	 * @return 实体类结果集
	 */
	List<T> getByIds(List<ID> ids);

	/**
	 * 当参数不在同一个表中,且结果集会跨表时使用
	 * 
	 * @param params 参数列表
	 * @return 结果集
	 */
	Result<List<?>> getLists(AbstractPager page, Specification<T> specification);

	/**
	 * 根据上级编号递归获得表中树形结构数据
	 * 
	 * @param id 上级编号
	 * @return 树形结果集
	 */
	List<Map<String, Object>> getTree(ID id);

	/**
	 * 该方法根据上级编号查询本级数据或下级数据
	 * 
	 * @param id 条件编号
	 * @param parent 是否为上级菜单编号,true是,false否
	 * @return 多行结果集
	 */
	List<Map<String, Object>> getTree(ID id, boolean parent);

	/**
	 * 判断某表中是否有重复的值
	 * 
	 * @param param 键值对,key表示java属性名,value表示查询的值
	 * @return true->有值,false->没有值
	 */
	boolean hasValue(Map<String, Object> param);

	/**
	 * 判断某表中是否有重复的值
	 * 
	 * @param column 表中字段名
	 * @param value 需要查询的值
	 * @return true->有值,false->没有值
	 */
	boolean hasValue(String column, Object value);

	/**
	 * 查询所有数据
	 * 
	 * @return 所有数据
	 */
	List<T> getEntitys();

	/**
	 * 分页查询数据
	 * 
	 * @param abstractPager 参数
	 * @return 分页数据
	 */
	Result<List<T>> getEntitys(AbstractPager abstractPager);

	/**
	 * 分页查询数据
	 * 
	 * @param pageable 分页参数
	 * @return 分页数据
	 */
	Result<List<T>> getEntitys(Pageable pageable);

	/**
	 * 分页查询数据
	 * 
	 * @param specification 分页条件
	 * @param abstractPager 分页参数
	 * @return 分页数据
	 */
	Result<List<T>> getEntitys(Specification<T> specification, AbstractPager abstractPager);

	/**
	 * 分页查询数据
	 * 
	 * @param specification 分页条件
	 * @param pageable 分页参数
	 * @return 分页数据
	 */
	Result<List<T>> getEntitys(Specification<T> specification, Pageable pageable);

	/**
	 * 分页/不分页查询实体类中数据,实体类中非null字段值才可作为查询条件
	 * 
	 * @param t 实体类参数
	 * @return 分页/不分页list
	 */
	Result<List<T>> getEntitys(T t);
}