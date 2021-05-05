package com.wy.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * mybatis mapper基础接口,不可放在com.wy.mapper中,扫描时会报异常,因为泛型无实际类型
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:44:21
 * @git {@link https://github.com/mygodness100}
 */
public interface BaseMappers<T, ID> extends BaseMapper<T> {

	/**
	 * 根据map中的参数进行分页或不分页查询,需要手动在xml中添加sql
	 * 
	 * @param map 参数
	 * @return 结果集
	 */
	List<Map<String, Object>> selectLists(Map<String, Object> map);

	/**
	 * 查询时间类字段在表中的最大值,只能是时间类型,需要手动在xml中添加sql
	 * 
	 * @param column
	 * @return
	 */
	Date getMaxTime(String column);
}