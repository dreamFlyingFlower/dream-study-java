package com.wy.base;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wy.collection.CollectionTool;
import com.wy.result.Result;

/**
 * 通用业务接口
 * 
 * @auther 飞花梦影
 * @date 2021-05-04 20:40:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface BaseService<T, ID extends Serializable> extends IService<T> {

	/**
	 * 数据新增,只会插入实体对象中的非空值
	 * 
	 * @param t 需要新增的数据
	 * @return 新增结果.默认返回boolean,true->新增成功,false->新增失败
	 */
	Object add(T t);

	/**
	 * 批量数据新增,只会插入实体对象中的非空值
	 * 
	 * @param ts 需要新增的数据列表
	 * @return 新增结果.默认返回boolean,true->新增成功,false->新增失败
	 */
	Object adds(List<T> ts);

	/**
	 * 数据修改,只会修改实体对象中的非空值
	 * 
	 * @param t 需要修改的数据
	 * @return 修改结果.默认返回boolean,true->修改成功,false->修改失败
	 */
	Object edit(T t);

	/**
	 * 批量数据修改,只会修改实体对象中的非空值
	 * 
	 * @param ts 需要修改的数据列表
	 * @return 修改结果.默认返回boolean,true->修改成功,false->修改失败
	 */
	Object edits(List<T> ts);

	/**
	 * 根据主键编号获得数据详情
	 * 
	 * @param id 主键编号
	 * @return 详情.默认返回实体类对象
	 */
	Object getDetail(ID id);

	/**
	 * 根据主键列表编号获得数据详情
	 * 
	 * @param ids 主编编号列表
	 * @return 详情列表.默认返回实体类对象列表
	 */
	Object getDetails(List<ID> ids);

	/**
	 * 分页/不分页查询.根据实体类对象参数中的非空值进行相等查询
	 * 
	 * @param t 实体类参数
	 * @return 数据集合
	 */
	Result<List<T>> getEntitys(T t);

	/**
	 * 分页/不分页查询.实际效果等同于{{@link #getEntitys(Object)},当有非实体类参数时,重写该方法自行处理
	 * 
	 * @param params 参数
	 * @return 数据集合
	 */
	default Result<List<Map<String, Object>>> getLists(Map<String, Object> params) {
		Result<List<T>> entitys = getEntitys(JSON.parseObject(JSON.toJSONString(params), getEntityClass()));
		if (Objects.nonNull(entitys) && CollectionTool.isNotEmpty(entitys.getData())) {
			List<Map<String, Object>> parseObject = JSON.parseObject(JSON.toJSONString(entitys.getData()),
					new TypeReference<List<Map<String, Object>>>() {
					});
			return Result.page(parseObject, entitys.getPageIndex(), entitys.getPageSize(), entitys.getTotal());
		}
		return Result.ok(Collections.emptyList());
	}

	/**
	 * 查询某个字段的最大值,只能是数字类型
	 * 
	 * @param column 字段名
	 * @return 最大值
	 */
	Object getMax(String column);

	/**
	 * 根据实体类中的所有参数查询是否有重复值
	 * 
	 * @param t 实体类对象
	 * @return 大于0有重复值
	 */
	int hasValue(T t);

	/**
	 * 根据实体类中的非空参数进行相等判断删除数据
	 * 
	 * @param t 实体数据
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	Object delete(T t);

	/**
	 * 根据主键删除单条数据
	 * 
	 * @param id 主键编号
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	Object deleteById(ID id);

	/**
	 * 根据主键删除多条数据
	 * 
	 * @param ids 主键编号列表
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	Object deleteByIds(List<ID> ids);
}