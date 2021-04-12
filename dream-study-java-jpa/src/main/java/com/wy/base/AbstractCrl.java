package com.wy.base;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.wy.annotation.ListModel;
import com.wy.result.Result;
import com.wy.result.ResultException;
import com.wy.valid.ValidCreates;
import com.wy.valid.ValidEdits;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public abstract class AbstractCrl<T, ID> {

	@Autowired
	protected BaseService<T, ID> baseService;

	@ApiOperation("通用单条数据新增")
	@PostMapping("create")
	public Result<?> create(@RequestBody @Validated(ValidCreates.class) T model, BindingResult bind) {
		if (bind.hasErrors()) {
			FieldError error = bind.getFieldError();
			assert error != null;
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		Object res = baseService.saveOrUpdate(model);
		return Objects.nonNull(res) ? Result.ok(res) : Result.error("新增失败");
	}

	@ApiOperation("批量数据新增,不检查任何有效性")
	@PostMapping("creates")
	public Result<?> creates(@RequestBody List<T> models) {
		Object beans = baseService.saveOrUpdates(models);
		return Objects.nonNull(beans) ? Result.ok(beans) : Result.error("新增失败");
	}

	@ApiOperation("根据主键更新表中的该条数据的全部字段,若是传null,则数据库中字段就为null")
	@PostMapping("edit")
	public Result<?> edit(@RequestBody @Validated(ValidEdits.class) T model, BindingResult bind) {
		if (bind.hasErrors()) {
			FieldError error = bind.getFieldError();
			assert error != null;
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		return Result.ok(baseService.update(model));
	}

	@ApiOperation("根据主键获得数据详情,主键类型是数字类型")
	@GetMapping("getById/{id}")
	public Result<?> getById(@ApiParam("数字主键编号") @PathVariable ID id) {
		return Result.ok(baseService.getById(id));
	}

	@ApiOperation("分页/不分页查询单表数据,参数为非null字段的值,且条件都是相等")
	@GetMapping("getEntitys")
	public Result<?> getEntitys(@ApiParam("该API实体类参数") T entity) {
		return Result.ok(baseService.getEntitys(entity));
	}

	@ApiOperation("复杂分页查询,多表链接")
	@GetMapping("getLists")
	public Result<?> getLists(@ApiParam("该API实体类参数") @RequestParam(required = false) Map<String, Object> params) {
		if (this.getClass().isAnnotationPresent(ListModel.class)) {
			ListModel pageModel = this.getClass().getAnnotation(ListModel.class);
			Class<? extends AbstractPager> pageClass = pageModel.value();
			try {
				if (null == params || params.isEmpty()) {
					return baseService.getLists(pageClass.newInstance(), null);
				} else {
					return baseService.getLists(JSON.parseObject(JSON.toJSONString(params), pageClass), null);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return Result.error();
		} else {
			throw new ResultException("该controller没有相应注解可以使用分页方法");
		}
	}

	@ApiOperation("分页/不分页查询单表数据,参数为非null字段的值,且条件都是相等")
	@GetMapping("getTree/{id}")
	public Result<?> getTree(@ApiParam("该API实体类参数") @PathVariable ID id) {
		return Result.ok(baseService.getTree(id));
	}

	@ApiOperation("查询该类中某个字段值是否重复,若有多个键值对,则只要有一个键值对中值重复,返回false")
	@PostMapping("hasValue")
	public Result<?> hasValue(@RequestBody Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return Result.error("参数为空");
		}
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (StringUtils.hasText(entry.getKey())) {
				if (baseService.hasValue(entry.getKey(), entry.getValue())) {
					return Result.ok(entry.getKey() + "字段值重复", 1);
				}
			}
		}
		return Result.ok("无重复值", 0);
	}

	@ApiOperation("根据主键删除表中单条数据,主键类型是数字类型")
	@GetMapping("remove/{id}")
	public Result<?> remove(@PathVariable ID id) {
		baseService.deleteById(id);
		return Result.ok();
	}

	@ApiOperation("根据主键批量删除表中数据,主键类型是数字类型")
	@PostMapping("removes")
	public Result<?> removes(@RequestBody List<ID> ids) {
		if (null == ids || ids.isEmpty()) {
			return Result.error("集合数据为空");
		}
		baseService.deleteByIds(ids);
		return Result.ok();
	}
}