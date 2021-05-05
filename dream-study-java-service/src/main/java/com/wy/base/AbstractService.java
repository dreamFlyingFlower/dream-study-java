package com.wy.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wy.collection.ListTool;
import com.wy.database.Sort;
import com.wy.database.Unique;
import com.wy.excel.ExcelModelTools;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.result.Result;
import com.wy.result.ResultException;

/**
 * 基础service层,通用service方法
 * 
 * @apiNote 所有继承该类的子类的泛型都不可相同,即不可有2个子类的泛型相同,否则启动报错;可不使用baseMapper
 * 
 * @author ParadiseWY
 * @date 2019-08-05 15:51:27
 * @git {@link https://github.com/mygodness100}
 */
@SuppressWarnings("unchecked")
public abstract class AbstractService<T, ID> implements BaseService<T, ID> {

	@Autowired
	public BaseMapper<T, ID> baseMapper;

	public Class<T> clazz;

	/**
	 * @apiNote 获得子类的泛型class
	 */
	public AbstractService() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		Type type = parameterizedType.getActualTypeArguments()[0];
		clazz = (Class<T>) type;
	}

	/**
	 * @apiNote 获得子类的泛型class
	 */
	public Class<T> getClassOfT() {
		return clazz;
	}

	/**
	 * @apiNote 利用pagehelper分页,也可以在xml中手动写limit
	 * @param pager 分页参数
	 * @return 分页信息
	 */
	public Page<Object> startPage(AbstractPager pager) {
		String pageDirection = pager.getPageDirection();
		if (pager.hasPager()) {
			if (StrTool.isNotBlank(pager.getPageOrder())) {
				return PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(),
						pager.getPageOrder() + " " + (StrTool.isBlank(pageDirection) ? " desc " : pageDirection));
			} else {
				return PageHelper.startPage(pager.getPageIndex(), pager.getPageSize());
			}
		}
		return null;
	}

	public Page<Object> startPage(Integer pageIndex, Integer pageSize) {
		if (pageIndex != null && pageIndex > 0) {
			if (pageSize == null || pageSize <= 0) {
				pageSize = 10;
			}
			return PageHelper.startPage(pageIndex, pageSize);
		}
		return null;
	}

	@Override
	@Transactional
	public Object insert(T t) {
		handlerUniqueAndSort(t, true, true);
		return baseMapper.insert(t);
	}

	@Override
	@Transactional
	public Object insertSelective(T t) {
		handlerUniqueAndSort(t, true, true);
		return baseMapper.insertSelective(t);
	}

	@Override
	@Transactional
	public Object inserts(List<T> ts) {
		baseMapper.inserts(ts);
		return ts;
	}

	/**
	 * 检查实体类中需要进行唯一性校验和排序的字段
	 * 
	 * @param model 实体类
	 * @param saveOrUpdate 新增或更新
	 * @param checkSort 是否检查排序,新增的时候默认检查,修改的时候不检查
	 */
	private void handlerUniqueAndSort(T model, boolean saveOrUpdate, boolean checkSort) {
		Field[] fields = clazz.getDeclaredFields();
		// Map<String, Object> map = new HashMap<>();
		for (Field field : fields) {
			// 检查是否有unique字段
			if (field.isAnnotationPresent(Unique.class)) {
				field.setAccessible(true);
				try {
					if (saveOrUpdate) {
						// 当新增时可以直接加入检查唯一的map中,不可多字段同时检查
						Map<String, Object> temp = new HashMap<>();
						temp.put(field.getName(), field.get(model));
						if (hasValue(JSON.parseObject(JSON.toJSONString(temp), clazz))) {
							throw new ResultException("参数有重复值,请检查");
						}
					} else {
						// 当更新时,检查原始值和新值是否相同,若相同,不用再查数据库,且需要将实体类中的该字段值置空
						Unique unique = field.getAnnotation(Unique.class);
						String oriName = unique.oriName();
						if (StrTool.isBlank(oriName)) {
							oriName = "ori" + StrTool.firstUpper(field.getName());
						}
						Field actualField = clazz.getDeclaredField(oriName);
						actualField.setAccessible(true);
						Object object = actualField.get(model);
						if (Objects.equals(object, field.get(model))) {
							field.set(model, null);
						} else {
							Map<String, Object> temp = new HashMap<>();
							temp.put(field.getName(), field.get(model));
							if (hasValue(JSON.parseObject(JSON.toJSONString(temp), clazz))) {
								throw new ResultException("参数有重复值,请检查");
							}
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			// 检查是否有排序字段
			if (checkSort && field.isAnnotationPresent(Sort.class)) {
				Long maxSort = validSort(field, model);
				if (maxSort == null) {
					maxSort = 0l;
				}
				if (maxSort == -1) {
					throw new ResultException("排序字段错误");
				} else {
					try {
						field.set(model, (int) maxSort.longValue() + 1);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						throw new ResultException("新增系统错误");
					}
				}
			}
		}
	}

	private Long validSort(Field field, T model) {
		field.setAccessible(true);
		try {
			Object value = field.get(model);
			if (Objects.nonNull(value)) {
				Long number = NumberTool.toLong(field.get(model).toString());
				if (number > 0) {
					return number;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return -1l;
		}
		Sort sort = field.getAnnotation(Sort.class);
		return baseMapper.getMaxValue(StrTool.isBlank(sort.value()) ? field.getName() : sort.value());
	}

	@Override
	@Transactional
	public int delete(ID id) {
		return baseMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int deletes(List<ID> ids) {
		return baseMapper.deleteByPrimaryKeys(ids);
	}

	@Override
	@Transactional
	public int clear() {
		return baseMapper.deleteAll();
	}

	@Override
	@Transactional
	public int update(T t) {
		handlerUniqueAndSort(t, false, false);
		return baseMapper.updateByPrimaryKey(t);
	}

	@Override
	@Transactional
	public int updateSelective(T t) {
		handlerUniqueAndSort(t, false, false);
		return baseMapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public boolean hasValue(T t) {
		return baseMapper.countByEntity(t) > 0;
	}

	@Override
	public Object getById(ID id) {
		return baseMapper.selectByPrimaryKey(id);
	}

	/**
	 * @apiNote 递归查询表中树形结构,需要重写getChildren方法.
	 * @param id 查询条件
	 * @param self 是否查询本级数据,true获取,false直接获取下级,默认false
	 * @param params 其他基本类型参数
	 * @return 树形结果集
	 */
	@Override
	public List<T> getTree(ID id, Boolean self, Map<String, Object> params) {
		List<T> trees = getLeaf(id, self == null ? false : self.booleanValue(), params);
		getLeaf(trees, params);
		return trees;
	}

	/**
	 * 该方法根据上级编号查询本级数据或下级数据.为统一前端树形结构,需要将标识符,
	 * 如id全部转为treeId,显示的名称都改为treeName.同时每次查询都需要将下级的数量查询出来,放入childNum字段中 {@link select b.dic_id
	 * treeId,b.dic_name treeName,b.dic_code dicCode, (select count(*) from td_dic a where a.pid =
	 * b.dic_Id) childNum from td_dic b}
	 * 
	 * @param id 条件编号
	 * @param self 是否查询本级数据,true获取,false直接获取下级
	 * @param params 其他基本类型参数
	 * @return 树形结果集
	 */
	@Override
	public List<T> getLeaf(ID id, boolean self, Map<String, Object> params) {
		return null;
	}

	public void getLeaf(List<T> trees, Map<String, Object> params) {
		if (ListTool.isEmpty(trees)) {
			return;
		}
		for (T t : trees) {
			if (!(t instanceof Tree)) {
				throw new ResultException("this class does not extends Tree");
			}
			Tree<T, ID> tree = (Tree<T, ID>) t;
			if (tree.getChildNum() > 0) {
				List<T> childs = getLeaf(tree.getTreeId(), false, params);
				getLeaf(childs, params);
				tree.setChildren(childs);
			}
		}
	}

	@Override
	public Result<List<T>> getEntitys(T t) {
		if (t != null && t instanceof AbstractPager) {
			startPage((AbstractPager) t);
			List<T> listByEntitys = baseMapper.selectEntitys(t);
			PageInfo<T> pageInfo = new PageInfo<>(listByEntitys);
			return Result.page(listByEntitys, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
		}
		return Result.ok(baseMapper.selectEntitys(t));
	}

	@Override
	public void getExport(T t, HttpServletRequest request, HttpServletResponse response) {
		Result<List<T>> entitys = getEntitys(t);
		String excelName = StrTool.isBlank(request.getParameter("excelName")) ? "数据导出"
				: request.getParameter("excelName");
		ExcelModelTools.getInstance().exportExcel(entitys.getData(), response, excelName);
	}

	@Override
	public Result<List<Map<String, Object>>> getLists(Map<String, Object> params) {
		if (params == null) {
			return Result.ok(baseMapper.selectLists(new HashMap<>()));
		}
		if (params.get("pageIndex") == null || NumberTool.toInt(params.get("pageIndex").toString()) < 0) {
			return Result.ok(baseMapper.selectLists(params));
		}
		int pageSize = 0;
		if (params.get("pageSize") == null || NumberTool.toInt(params.get("pageSize").toString()) <= 0) {
			pageSize = 10;
		} else {
			pageSize = NumberTool.toInt(params.get("pageSize").toString());
		}
		PageHelper.startPage(NumberTool.toInt(params.get("pageIndex").toString()), pageSize);
		List<Map<String, Object>> lists = baseMapper.selectLists(params);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(lists);
		return Result.page(lists, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
	}
}