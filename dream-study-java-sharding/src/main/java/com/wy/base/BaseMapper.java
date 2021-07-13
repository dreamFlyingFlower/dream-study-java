package com.wy.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * mybatis mapper基础接口,不可放在com.wy.mapper中,扫描时会报异常,因为泛型无实际类型
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:44:21
 * @git {@link https://github.com/mygodness100}
 */
public interface BaseMapper<T,ID> {

	/**
	 * 新增数据,即使是null也会新增到表中
	 * 
	 * @param t 实体数据
	 * @return 影响行数
	 */
	int insert(T t);

	/**
	 * 新增数据,非null数据才会添加到表中
	 * 
	 * @param t 实体数据
	 * @return 影响行数
	 */
	int insertSelective(T t);

	/**
	 * 批量新增,所有字段任意值都会写入表中
	 * 
	 * @param ts 数据集合
	 */
	void inserts(List<T> ts);

	/**
	 * 根据id删除数据
	 * 
	 * @param id 主键id
	 * @return 影响行数
	 */
	int deleteByPrimaryKey(ID id);

	/**
	 * 根据id批量删除数据,自定义方法,每个xml中必须手动添加
	 * 
	 * @param ids 主键集合
	 * @return 影响行数
	 */
	int deleteByPrimaryKeys(List<ID> list);

	/**
	 * 删除表中所有数据
	 * 
	 * @return 影响行数
	 */
	int deleteAll();

	/**
	 * 根据主键修改数据,若是数据为null,表中数据也会置为null
	 * 
	 * @param t 要修改的数据
	 * @return 影响行数
	 */
	int updateByPrimaryKey(T t);

	/**
	 * 根据主键修改数据,只会将参数中的非null数据更新到表中
	 * 
	 * @param t 要修改的数据
	 * @return 影响行数
	 */
	int updateByPrimaryKeySelective(T t);

	/**
	 * 根据主键id查找信息
	 * 
	 * @param id 主键id
	 * @return 详情
	 */
	T selectByPrimaryKey(ID id);

	/**
	 * 根据实体中的非null参数进行分页或不分页查询,条件只能是相等
	 * 
	 * @param t 实体参数
	 * @return 结果集
	 */
	List<T> selectEntitys(T t);

	/**
	 * 根据map中的参数进行分页或不分页查询,需要自行编写sql
	 * 
	 * @param map 参数
	 * @return 结果集
	 */
	List<Map<String, Object>> selectLists(Map<String, Object> map);

	/**
	 * 根据实体中的非null参数进行计数,条件只能相等,更复杂的可使用countByExample或自定义,需要在xml中手动添加sql
	 * 
	 * @param t 实体参数
	 * @return 结果
	 */
	Long countByEntity(T t);

	/**
	 * 查询对应字段在表中的当前最大值,只能是数字类,需要手动在xml中添加sql
	 * 
	 * @param column java字段
	 * @return 最大值
	 */
	Long getMaxValue(@Param("column") String column);

	/**
	 * 查询时间类字段在表中的最大值,只能是时间类型,需要手动在xml中添加sql
	 * 
	 * @param column
	 * @return
	 */
	Date getMaxTime(String column);
}