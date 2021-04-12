package com.wy.base;

import java.util.List;
import java.util.Objects;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wy.collection.ListTool;
import com.wy.result.Result;
import com.wy.valid.ValidCreates;
import com.wy.valid.ValidEdits;

import io.swagger.annotations.ApiOperation;

/**
 * 基础控制层,需要重新设置权限 FIXME
 * 
 * @author ParadiseWY
 * @date 2020年4月2日 下午4:34:17
 */
public abstract class AbstractCrl<T,ID> extends QueryCrl<T,ID> {

	/**
	 * 通用单条数据新增,null数据不新增,暂时不考虑权限,每个权限不一样,需要重新设计
	 * 
	 * @param model 需要新增的数据
	 * @param bind 字段校验
	 * @return 新增是否成功
	 */
	@ApiOperation("单条数据新增")
	@PostMapping("create")
	public Result<?> create(@RequestBody @Validated(ValidCreates.class) T t, BindingResult bind) {
		if (bind.hasErrors()) {
			FieldError error = bind.getFieldError();
			assert error != null;
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		return Result.ok(abstractService.insertSelective(t));
	}

	@ApiOperation("批量数据新增,不检查任何有效性")
	@PostMapping("creates")
	public Result<?> creates(@RequestBody List<T> ts) {
		Object beans = abstractService.inserts(ts);
		return Objects.nonNull(beans) ? Result.ok(beans) : Result.error("新增失败");
	}

	@ApiOperation("根据主键删除表中单条数据,主键类型是数字类型")
	@GetMapping("remove/{id}")
	public Result<?> remove(@PathVariable ID id) {
		int row = abstractService.delete(id);
		return row > 0 ? Result.ok(row) : Result.error("删除失败");
	}

	@ApiOperation("根据主键批量删除表中数据,主键类型是数字类型")
	@PostMapping("removes")
	public Result<?> removes(@RequestBody List<ID> ids) {
		if (ListTool.isEmpty(ids)) {
			return Result.error("集合数据为空");
		}
		return Result.ok(abstractService.deletes(ids));
	}

	@ApiOperation("根据主键更新表中的该条数据的全部字段,若是传null,则数据库中字段就为null")
	@PostMapping("edit")
	public Result<?> edit(@RequestBody @Validated(ValidEdits.class) T t, BindingResult bind) {
		if (bind.hasErrors()) {
			FieldError error = bind.getFieldError();
			assert error != null;
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		return Result.ok(abstractService.updateSelective(t) > 0);
	}
}