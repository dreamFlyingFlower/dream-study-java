package com.wy.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wy.collection.ListTool;
import com.wy.database.Sort;
import com.wy.database.Unique;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.reflect.ReflectTool;
import com.wy.result.Result;
import com.wy.result.ResultException;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用业务实现类
 * 
 * @auther 飞花梦影
 * @date 2021-05-05 00:14:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public abstract class AbstractService<T, ID extends Serializable> extends ServiceImpl<BaseMapper<T>, T>
		implements BaseService<T, ID> {

	/** 存储反射过程中使用的类和字段 */
	private Map<Class<?>, List<Field>> CACHE_FIELDS = new ConcurrentHashMap<>(128);

	/**
	 * 数据新增,只会插入实体对象中的非空值
	 * 
	 * @param t 需要新增的数据
	 * @return 新增结果.默认返回boolean,true->新增成功,false->新增失败
	 */
	@Override
	public Object add(T t) {
		checkUniqueAndSort(t, true, true);
		return super.save(t);
	}

	/**
	 * 检查实体类中需要进行唯一性校验和排序的字段
	 * 
	 * @param t 实体类对象
	 * @param saveOrUpdate 新增或更新,true->新增,false->修改
	 * @param checkSort 是否检查排序,新增的时候默认检查,修改的时候不检查.true->检查,false->不检查
	 */
	protected void checkUniqueAndSort(T t, boolean saveOrUpdate, boolean checkSort) {
		Class<?> searchType = entityClass;
		while (Object.class != searchType && searchType != null) {
			List<Field> fields = CACHE_FIELDS.get(searchType);
			if (ListTool.isEmpty(fields)) {
				fields = Arrays.asList(searchType.getDeclaredFields());
				CACHE_FIELDS.put(searchType, (fields.size() == 0 ? Collections.emptyList() : fields));
			}
			if (ListTool.isNotEmpty(fields)) {
				for (Field field : fields) {
					checkUniqueAndSort(t, field, saveOrUpdate, checkSort);
				}
			}
			searchType = searchType.getSuperclass();
		}
	}

	/**
	 * 检查实体类中需要进行唯一性校验和排序的字段
	 * 
	 * @param t 实体类对象
	 * @param field 待检查的Java属性
	 * @param saveOrUpdate 新增或更新,true->新增,false->修改
	 * @param checkSort 是否检查排序,新增的时候默认检查,修改的时候不检查.true->检查,false->不检查
	 */
	protected void checkUniqueAndSort(T t, Field field, boolean saveOrUpdate, boolean checkSort) {
		// 检查是否有unique字段
		if (field.isAnnotationPresent(Unique.class)) {
			checkUnique(t, field, saveOrUpdate);
		}
		// 检查是否有排序字段
		if (checkSort && field.isAnnotationPresent(Sort.class)) {
			checkSort(t, field);
		}
	}

	/**
	 * 检查实体类中需要进行唯一性校验的Java属性
	 * 
	 * @param t 实体类对象
	 * @param field 待检查的Java属性
	 * @param saveOrUpdate 新增或更新,true->新增,false->修改
	 */
	protected void checkUnique(T t, Field field, boolean saveOrUpdate) {
		ReflectTool.fixAccessible(field);
		try {
			if (saveOrUpdate) {
				// 当新增时可以直接加入检查唯一的map中,不可多字段同时检查
				checkUnique(t, field);
			} else {
				// 当更新时,检查原始值和新值是否相同,若相同,不用再查数据库,且需要将实体类中的该字段值置空
				Unique unique = field.getAnnotation(Unique.class);
				// 获得原始值Java属性
				String oriName = unique.oriName();
				if (StrTool.isBlank(oriName)) {
					oriName = "ori" + StrTool.firstUpper(field.getName());
				}
				Field oriField = entityClass.getDeclaredField(oriName);
				ReflectTool.fixAccessible(oriField);
				Object object = oriField.get(t);
				if (Objects.equals(object, field.get(t))) {
					// 原始值和新值相同,将新值置空,不更新数据库
					field.set(t, null);
				} else {
					// 原始值和新值不同,查询数据库是否有重复值
					checkUnique(t, field);
				}
			}
		} catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 检查原始值和新值不同,查询数据库是否有重复值
	 * 
	 * @param t 实体类对象
	 * @param field 待检查的Java属性
	 */
	protected void checkUnique(T t, Field field) {
		Map<String, Object> temp = new HashMap<>();
		try {
			temp.put(field.getName(), field.get(t));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		if (hasValue(JSON.parseObject(JSON.toJSONString(temp), entityClass)) > 0) {
			throw new ResultException("参数有重复值,请检查");
		}
	}

	/**
	 * 检查是否有需要排序的字段
	 * 
	 * @param t 实体类对象
	 * @param field 待检查的Java属性
	 */
	protected void checkSort(T t, Field field) {
		ReflectTool.fixAccessible(field);
		// 获得排序字段的值
		Long maxSort = getMaxSort(t, field);
		if (null == maxSort) {
			maxSort = 0l;
		}
		if (-1 == maxSort) {
			throw new ResultException("排序字段错误或不存在");
		} else {
			try {
				field.set(t, (int) maxSort.longValue() + 1);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				throw new ResultException("排序错误");
			}
		}
	}

	/**
	 * 获得排序字段的值
	 * 
	 * @param t 实体类对象
	 * @param field 待检查的Java属性
	 */
	protected Long getMaxSort(T t, Field field) {
		try {
			// 若排序字段有值且大于0,直接返回
			Object value = field.get(t);
			if (Objects.nonNull(value)) {
				Long number = NumberTool.toLong(field.get(t).toString());
				if (number > 0) {
					return number;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return -1l;
		}
		Sort sort = field.getAnnotation(Sort.class);
		return getMax(StrTool.isBlank(sort.value())
				? sort.hump2Snake() ? StrTool.hump2Snake(field.getName()) : field.getName()
				: sort.value(), false);
	}

	/**
	 * 批量数据新增,只会插入实体对象中的非空值
	 * 
	 * @param ts 需要新增的数据列表
	 * @return 新增结果.默认返回boolean,true->新增成功,false->新增失败
	 */
	@Override
	public Object adds(List<T> ts) {
		return super.saveBatch(ts);
	}

	/**
	 * 数据修改,只会修改实体对象中的非空值
	 * 
	 * @param t 需要修改的数据
	 * @return 修改结果.默认返回boolean,true->修改成功,false->修改失败
	 */
	@Override
	public Object edit(T t) {
		checkUniqueAndSort(t, false, false);
		return super.updateById(t);
	}

	/**
	 * 批量数据修改,只会修改实体对象中的非空值
	 * 
	 * @param ts 需要修改的数据列表
	 * @return 修改结果.默认返回boolean,true->修改成功,false->修改失败
	 */
	@Override
	public Object edits(List<T> ts) {
		return super.updateBatchById(ts);
	}

	/**
	 * 根据主键编号获得数据详情
	 * 
	 * @param id 主键编号
	 * @return 详情.默认返回实体类对象
	 */
	@Override
	public Object getDetail(ID id) {
		return super.getById(id);
	}

	/**
	 * 根据主键列表编号获得数据详情列表
	 * 
	 * @param ids 主编编号列表
	 * @return 详情列表.默认返回实体类对象列表
	 */
	@Override
	public Object getDetails(List<ID> ids) {
		return super.listByIds(ids);
	}

	/**
	 * 分页/不分页查询.根据实体类对象参数中的非空值进行相等查询 FIXME 添加createtime,updatetime
	 * 
	 * @param t 实体类对象参数
	 * @return 列表结果.默认返回实体类对象列表
	 */
	@Override
	public Object getEntitys(T t) {
		if (t == null || !(t instanceof AbstractPager)) {
			return super.list(new QueryWrapper<T>(t));
		}
		AbstractPager pager = (AbstractPager) t;
		if (!pager.hasPager()) {
			return super.list(new QueryWrapper<T>(t));
		}
		Page<T> pageParam = new Page<T>(pager.getPageIndex(), pager.getPageSize());
		if (StrTool.isNotBlank(pager.getPageOrder())) {
			if (StrTool.equalsIgnoreCase("asc", pager.getPageDirection())) {
				pageParam.setOrders(OrderItem.ascs(pager.getPageOrder().split(",")));
			} else {
				pageParam.setOrders(OrderItem.descs(pager.getPageOrder().split(",")));
			}
		}
		Page<T> page = page(pageParam, new QueryWrapper<T>(t));
		return null == page ? Result.ok()
				: Result.page(page.getRecords(), pager.getPageIndex(), pager.getPageSize(), page.getTotal());
	}

	/**
	 * 获得指定字段的最大值,只能是数字类型字段
	 * 
	 * @param column Java属性字段,会自动转下划线
	 * @return 最大值
	 */
	@Override
	public Long getMax(String column) {
		return getMax(column, true);
	}

	/**
	 * 获得指定字段的最大值,只能是数字类型字段
	 * 
	 * @param column Java属性字段,会自动转下划线
	 * @param hump2Snake 是否转下划线,true->转,false->不转
	 * @return 最大值
	 */
	public Long getMax(String column, boolean hump2Snake) {
		// 将Java属性字段转下划线
		column = hump2Snake ? StrTool.hump2Snake(column) : column;
		Page<Map<String, Object>> pageParam = new Page<>(1, 1);
		pageParam.addOrder(OrderItem.desc(column));
		Page<Map<String, Object>> pageMaps = pageMaps(pageParam);
		if (Objects.isNull(pageMaps) || pageMaps.getTotal() == 0) {
			return 0l;
		}
		return Long.parseLong(pageMaps.getRecords().get(0).get(column) + "");
	}

	/**
	 * 根据实体类中的所有参数查询是否有重复值
	 * 
	 * @param t 实体类对象
	 * @return 大于0有重复值
	 */
	@Override
	public int hasValue(T t) {
		return super.count(new QueryWrapper<T>(t));
	}

	/**
	 * 根据主键删除单条数据
	 * 
	 * @param id 主键编号
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	@Override
	public Object remove(ID id) {
		return super.removeById(id);
	}

	/**
	 * 根据主键删除多条数据
	 * 
	 * @param ids 主键编号列表
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	@Override
	public Object removes(List<ID> ids) {
		return super.removeByIds(ids);
	}
}