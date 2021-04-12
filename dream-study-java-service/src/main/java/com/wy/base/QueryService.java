package com.wy.base;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wy.result.Result;

/**
 * 只负责查询的业务接口
 * 
 * @author ParadiseWY
 * @param <K>
 * @date 2020年5月20日 上午11:58:32
 */
public interface QueryService<T,ID> {

	/**
	 * @apiNote 判断表中是否有重复的值,只查询非null字段,需要每个xml中都手写countByEntity方法
	 * @param t 需要查询的值
	 * @return true代表有重复值,false代表无重复值
	 */
	boolean hasValue(T t);

	/**
	 * 根据表中主键查询单条数据详情
	 * 
	 * @param id 主键编号
	 * @return 单实体类结果集
	 */
	Object getById(ID id);

	/**
	 * 根据上级编号递归获得表中树形结构数据
	 * 
	 * @param id 上级编号
	 * @param self 是否查询本级数据,true获取,false直接获取下级,默认false
	 * @param params 其他基本类型参数
	 * @return 树形结果集
	 */
	List<T> getTree(ID id, Boolean self, Map<String, Object> params);

	/**
	 * 该方法根据上级编号查询本级数据或下级数据
	 * 
	 * @param id 条件编号
	 * @param self 是否查询本级数据,true获取,false直接获取下级,默认false
	 * @param params 其他基本类型参数
	 * @return 多行结果集
	 */
	List<T> getLeaf(ID id, boolean parent, Map<String, Object> params);

	/**
	 * 分页/不分页查询实体类中数据,实体类中非null以及非""字段值才可作为查询条件,条件只能是等于
	 * 
	 * @param t 实体类参数
	 * @return 分页/不分页list
	 */
	Result<List<T>> getEntitys(T t);

	/**
	 * 单表数据导出功能,默认数据来源于getEntitys方法
	 * 
	 * @param t 实体类参数,若beginCreatetime,endCreatetime,beginUpdatetime,endUpdatetime有值,
	 *        则会和createtime,updatetime字段比较
	 * @param request 请求,主要是传入需要导出的excel名称
	 * @param response 响应
	 */
	void getExport(T t, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 该方法参数以及返回值都是map类型
	 * 
	 * @param params 分页时需要传pageIndex和pageSize,可传特殊类型参数,需要特殊处理,就是一个map,可能后期会做些其他处理
	 * @return 分页/不分页list
	 */
	Result<List<Map<String, Object>>> getLists(Map<String, Object> params);
}