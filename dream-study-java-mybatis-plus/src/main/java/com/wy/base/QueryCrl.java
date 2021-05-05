package com.wy.base;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.wy.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 通用查询控制层API
 * 
 * @auther 飞花梦影
 * @date 2021-05-04 20:24:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "通用查询控制层API")
public abstract class QueryCrl<T, ID extends Serializable> {

	@Autowired
	protected BaseService<T, ID> baseService;

	/**
	 * 根据主键查找数据
	 * 
	 * @param id 主键编号
	 * @return 查询数据
	 */
	@ApiOperation("根据主键查找数据")
	@GetMapping("getById/{id}")
	public Result<?> getById(@ApiParam @PathVariable ID id) {
		return Result.ok(baseService.getDetail(id));
	}

	/**
	 * 根据实体类中的非空参数获得数据集合
	 * 
	 * @param t 实体类参数
	 * @return 数据集合
	 */
	@ApiOperation("根据主键查找数据")
	@GetMapping("getEntitys")
	public Result<?> getEntitys(@ApiParam T t) {
		return Result.ok(baseService.getEntitys(t));
	}

	/**
	 * 根据Map中的所有参数获得数据集合
	 * 
	 * @param params 参数
	 * @return 数据集合
	 */
	@ApiOperation("根据Map中的所有参数获得数据集合")
	@GetMapping("getLists")
	public Result<?> getLists(@ApiParam @RequestParam Map<String, Object> params) {
		return Result.ok(baseService.getLists(params));
	}

	/**
	 * 获得指定字段的最大值,只能是数字类型字段
	 * 
	 * @param column Java属性字段,会自动转下划线
	 * @return 最大值,可能为0
	 */
	@ApiOperation("获得指定字段的最大值,只能是数字类型字段")
	@GetMapping("getMax/{column}")
	public Result<?> getMax(@ApiParam("需要查询最大值的Java字段,该字段类型必须是数字类型") @PathVariable String column) {
		return Result.ok(baseService.getMax(column));
	}

	/**
	 * 根据实体类中的参数查询是否有重复值
	 * 
	 * @param t 实体类对象
	 * @return data值大于0表示有重复值
	 */
	@ApiOperation("根据实体类中的参数查询是否有重复值")
	@PostMapping("hasValue")
	public Result<?> hasValue(@ApiParam @RequestBody T t) {
		return Result.ok(baseService.hasValue(t));
	}
}