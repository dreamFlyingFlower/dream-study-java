package com.wy.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.wy.result.Result;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 通用查询API
 * 
 * @author ParadiseWY
 * @date 2020年5月20日 上午11:41:11
 */
public abstract class QueryCrl<T,ID> {

	@Autowired
	public AbstractService<T,ID> abstractService;

	/**
	 * 查询数据库中的值是否有重复,条件必须是相等的才可以查询
	 * 
	 * @param t 实体类参数
	 * @return 是否有重复值,1为有,0没有
	 */
	@ApiOperation("查询该类中某个字段值是否重复")
	@PostMapping("hasValue")
	public Result<?> hasValue(@RequestBody T t) {
		if (abstractService.hasValue(t)) {
			return Result.ok("字段值重复", 1);
		}
		return Result.ok("无重复值", 0);
	}

	/**
	 * 根据主键编号获得数据详情,主键类型是数字类型
	 * 
	 * @param id 主键编号
	 * @return 详情数据
	 */
	@ApiOperation("根据主键获得数据详情,主键类型是数字类型")
	@GetMapping("getById/{id}")
	public Result<?> getById(@ApiParam("数字主键编号") @PathVariable ID id) {
		return Result.ok(abstractService.getById(id));
	}

	/**
	 * 根据主键编号查询该数据表中的树形结构数据,只往下查
	 * 
	 * @param id 数据表主键编号
	 * @param self 是否查询本级数据,true获取,false直接获取下级,默认false
	 * @param params 其他基本类型参数
	 * @return 结果集
	 */
	@ApiOperation("查询单表中的树形接口数据")
	@GetMapping("getTree/{id}")
	public Result<?> getTree(@ApiParam("该API实体类参数") @PathVariable ID id,
			@ApiParam("是否查询本级数据,true获取,false直接获取下级,默认false") @RequestParam(required = false) Boolean self,
			@ApiParam("其他基本类型参数") @RequestParam(required = false) Map<String, Object> params) {
		return Result.ok(abstractService.getTree(id, self, params));
	}

	/**
	 * 分页/不分页查询,参数为非null字段的等值查询,除createtime和updatetime字段会根据传参的值进行比较
	 * 
	 * @param t 实体类,若beginCreatetime,endCreatetime,beginUpdatetime,endUpdatetime有值,
	 *        则会和createtime,updatetime字段比较
	 * @return 结果集
	 */
	@ApiOperation("分页/不分页查询,参数为非null字段的等值查询,该方法的返回值是以实体类为单位的结果集")
	@GetMapping("getEntitys")
	public Result<?> getEntitys(@ApiParam("该API实体类参数") T t) {
		return abstractService.getEntitys(t);
	}

	/**
	 * 单表数据导出功能,默认数据来源于getEntitys方法
	 * 
	 * @param t 实体类参数,若beginCreatetime,endCreatetime,beginUpdatetime,endUpdatetime有值,
	 *        则会和createtime,updatetime字段比较
	 * @param request 请求,可包含excelName参数,需要导出的excel名称,可不带后缀;其他指定参数
	 * @param response 响应
	 */
	@GetMapping("getExport")
	public void getExport(@ApiParam("该API实体类参数") T t, HttpServletRequest request, HttpServletResponse response) {
		abstractService.getExport(t, request, response);
	}

	/**
	 * 分页/不分页查询,需要自定义查询条件,该方法的返回值是以map为单位的查询方法
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation("分页/不分页查询,需要自定义查询条件,该方法的返回值是以map为单位的结果集")
	@GetMapping("getLists")
	public Result<?> getLists(@RequestParam(required = false) Map<String, Object> params) {
		return abstractService.getLists(params);
	}
}