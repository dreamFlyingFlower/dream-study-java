package com.wy.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wy.collection.ListTool;
import com.wy.enums.TipEnum;
import com.wy.result.Result;
import com.wy.valid.ValidAdds;
import com.wy.valid.ValidEdits;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 通用修改控制层API
 * 
 * @auther 飞花梦影
 * @date 2021-05-04 20:24:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "通用修改控制层API")
public abstract class AbstractCrl<T, ID extends Serializable> extends QueryCrl<T, ID> {

	/**
	 * 数据新增,只会插入实体对象中的非空值
	 * 
	 * @param t 需要新增的数据
	 * @param bindingResult 参数校验结果
	 * @return 新增后的数据
	 */
	@ApiOperation("数据新增,只会插入实体对象中的非空值")
	@PostMapping("add")
	public Result<?> add(@ApiParam("需要新增的数据") @Validated(ValidAdds.class) @RequestBody T t,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError error = bindingResult.getFieldError();
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		return Result.ok(baseService.add(t));
	}

	/**
	 * 批量数据新增,只会插入实体对象中的非空值
	 * 
	 * @param ts 需要新增的数据列表
	 * @param bindingResult 参数校验结果
	 * @return 新增后的数据
	 */
	@ApiOperation("批量数据新增,只会插入实体对象中的非空值")
	@PostMapping("adds")
	public Result<?> adds(@ApiParam("需要新增的数据列表") @RequestBody List<T> ts, BindingResult bindingResult) {
		if (ListTool.isEmpty(ts)) {
			return Result.error(TipEnum.TIP_PARAM_NOT_NULL);
		}
		return Result.ok(baseService.adds(ts));
	}

	/**
	 * 数据修改,只会修改实体对象中的非空值
	 * 
	 * @param t 需要修改的数据
	 * @param bindingResult 参数校验结果
	 * @return 修改后的结果
	 */
	@ApiOperation("数据修改,只会修改实体对象中的非空值")
	@PostMapping("edit")
	public Result<?> edit(@ApiParam("需要修改的数据") @Validated(ValidEdits.class) @RequestBody T t,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError error = bindingResult.getFieldError();
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		return Result.ok(baseService.edit(t));
	}

	/**
	 * 批量数据修改,只会修改实体对象中的非空值
	 * 
	 * @param ts 需要修改的数据列表
	 * @param bindingResult 参数校验结果
	 * @return 修改后的结果
	 */
	@ApiOperation("批量数据修改,只会修改实体对象中的非空值")
	@PostMapping("edits")
	public Result<?> edits(@ApiParam("需要修改的数据列表") @RequestBody List<T> ts, BindingResult bindingResult) {
		if (ListTool.isEmpty(ts)) {
			return Result.error(TipEnum.TIP_PARAM_NOT_NULL);
		}
		return Result.ok(baseService.edits(ts));
	}

	/**
	 * 根据主键删除单条数据
	 * 
	 * @param id 主键编号
	 * @return 影响行数
	 */
	@ApiOperation("根据主键删除单条数据")
	@GetMapping("remove/{id}")
	public Result<?> remove(@ApiParam("主键编号") @PathVariable ID id) {
		return Result.ok(baseService.remove(id));
	}

	/**
	 * 根据主键删除多条数据
	 * 
	 * @param ids 主键编号列表
	 * @return 影响行数
	 */
	@ApiOperation("根据主键删除单条数据")
	@PostMapping("removes")
	public Result<?> removes(@ApiParam("主键编号列表") @RequestBody List<ID> ids) {
		return Result.ok(baseService.removes(ids));
	}
}