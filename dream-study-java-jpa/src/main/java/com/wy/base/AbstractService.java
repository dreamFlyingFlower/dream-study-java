package com.wy.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.wy.result.Result;
import com.wy.result.ResultException;

/**
 * 通用业务实现类
 * 
 * @author 飞花梦影
 * @date 2021-01-06 22:43:40
 * @git {@link https://github.com/mygodness100}
 */
public abstract class AbstractService<T, ID> implements BaseService<T, ID> {

	@Autowired
	protected BaseRepository<T, ID> baseRepository;

	private Class<T> classOfT;

	private Class<ID> classOfID;

	@SuppressWarnings("unchecked")
	public AbstractService() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		Type typeOfT = parameterizedType.getActualTypeArguments()[0];
		Type typeOfID = parameterizedType.getActualTypeArguments()[1];
		classOfT = (Class<T>) typeOfT;
		classOfID = (Class<ID>) typeOfID;
	}

	protected Class<T> getClassOfT() {
		return classOfT;
	}

	protected Class<ID> getClassOfID() {
		return classOfID;
	}

	@Override
	public void count() {
		baseRepository.count();
	}

	@Override
	public void count(Specification<T> specification) {
		baseRepository.count(specification);
	}

	@Override
	public void delete(T t) {
		baseRepository.delete(t);
	}

	@Override
	public void deleteAll() {
		// 查找所有的数据之后再循环删除
		// baseRepository.deleteAll();
		// 直接删除表中数据
		baseRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(ID id) {
		baseRepository.deleteById(id);
	}

	@Override
	public void deleteByIds(List<ID> ids) {
		for (ID id : ids) {
			deleteById(id);
		}
	}

	@Override
	public void deletes(List<T> entities) {
		// 实际上是循环利用id单个删除
		// baseRepository.deleteAll(entities);
		// 真批量删除
		baseRepository.deleteInBatch(entities);
	}

	@Override
	public T getById(ID id) {
		return baseRepository.findById(id).get();
	}

	@Override
	public List<T> getByIds(List<ID> ids) {
		return baseRepository.findAllById(ids);
	}

	@Override
	public List<T> getEntitys() {
		return baseRepository.findAll();
	}

	/**
	 * 分页查询数据
	 * 
	 * @param abstractPager 参数
	 * @return 分页数据
	 */
	public Result<List<T>> getEntitys(AbstractPager page) {
		Sort sort = null;
		if (StringUtils.hasText(page.getPageOrder())) {
			sort = Sort.by(Direction.fromOptionalString(page.getPageDirection()).orElse(Direction.ASC),
					page.getPageOrder().split(","));
		}
		PageRequest pageRequest = PageRequest.of(page.getPageIndex() - 1, page.getPageSize(), sort);
		Page<T> result = baseRepository.findAll(pageRequest);
		return null == result ? Result.page(null, page.getPageIndex(), page.getPageSize(), 0)
				: Result.page(result.getContent(), page.getPageIndex(), page.getPageSize(), result.getTotalElements());
	}

	@Override
	public Result<List<T>> getEntitys(Pageable pageable) {
		Page<T> result = baseRepository.findAll(pageable);
		return null == result ? Result.page(null, pageable.getPageNumber(), pageable.getPageSize(), 0)
				: Result.page(result.getContent(), pageable.getPageNumber(), pageable.getPageSize(),
						result.getTotalElements());
	}

	@Override
	public Result<List<T>> getEntitys(Specification<T> specification, AbstractPager page) {
		Sort sort = null;
		if (StringUtils.hasText(page.getPageOrder())) {
			sort = Sort.by(Direction.fromOptionalString(page.getPageDirection()).orElse(Direction.ASC),
					page.getPageOrder().split(","));
		}
		PageRequest pageRequest = PageRequest.of(page.getPageIndex() - 1, page.getPageSize(), sort);
		Page<T> result = baseRepository.findAll(specification, pageRequest);
		return null == result ? Result.page(null, page.getPageIndex(), page.getPageSize(), 0)
				: Result.page(result.getContent(), page.getPageIndex(), page.getPageSize(), result.getTotalElements());
	}

	@Override
	public Result<List<T>> getEntitys(Specification<T> specification, Pageable pageable) {
		Page<T> result = baseRepository.findAll(specification, pageable);
		return null == result ? Result.page(null, pageable.getPageNumber(), pageable.getPageSize(), 0)
				: Result.page(result.getContent(), pageable.getPageNumber(), pageable.getPageSize(),
						result.getTotalElements());
	}

	/**
	 * 若有分页参数则分页,若没有,则查询全部符合条件的数据
	 * 
	 * @param model 实体类参数,非null作为=参数,若有其他条件参数,使用{@link #getLists(AbstractPager)}
	 * @return 分页数据或全部数据
	 */
	@Override
	public Result<List<T>> getEntitys(T model) {
		if (model instanceof AbstractPager) {
			AbstractPager page = (AbstractPager) model;
			Sort sort = null;
			if (StringUtils.hasText(page.getPageOrder())) {
				sort = Sort.by(Direction.fromOptionalString(page.getPageDirection()).orElse(Direction.ASC),
						page.getPageOrder().split(","));
			}
			PageRequest pageRequest = PageRequest.of(page.getPageIndex() - 1, page.getPageSize(), sort);
			Page<T> result = null;
			if (StringUtils.hasText(page.getBeginCreatetime()) || StringUtils.hasText(page.getEndCreatetime())) {
				Specification<T> specification = new Specification<T>() {

					private static final long serialVersionUID = 1L;

					List<Predicate> cnds = new ArrayList<>();

					@Override
					public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
							CriteriaBuilder criteriaBuilder) {
						Field[] fields = model.getClass().getDeclaredFields();
						for (Field field : fields) {
							field.setAccessible(true);
							try {
								if (Objects.nonNull(field.get(model))) {
									if ("createtime".equals(field.getName())) {
										if (StringUtils.hasText(page.getBeginCreatetime())) {
											cnds.add(criteriaBuilder.greaterThanOrEqualTo(root.get(field.getName()),
													page.getBeginCreatetime()));
										}
										if (StringUtils.hasText(page.getEndCreatetime())) {
											cnds.add(criteriaBuilder.lessThanOrEqualTo(root.get(field.getName()),
													page.getEndCreatetime()));
										}
									} else {
										cnds.add(criteriaBuilder.equal(root.get(field.getName()).as(field.getType()),
												field.get(model)));
									}
								}
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
						}
						return criteriaBuilder.and(cnds.toArray(new Predicate[cnds.size()]));
					}
				};
				result = baseRepository.findAll(specification, pageRequest);
			} else {
				result = baseRepository.findAll(Example.of(model), pageRequest);
			}
			return null == result ? Result.page(null, page.getPageIndex(), page.getPageSize(), 0)
					: Result.page(result.getContent(), page.getPageIndex(), page.getPageSize(),
							result.getTotalElements());
		}
		List<T> result = baseRepository.findAll(Example.of(model));
		return Result.ok(result);
	}

	@Override
	public Result<List<?>> getLists(AbstractPager page, Specification<T> specification) {
		if (null == specification) {
			// return getEntitys((T)page);
		}
		return null;
	}

	/**
	 * 递归查询表中树形结构,需要重写getChildren方法.
	 * 
	 * @param id 查询条件
	 * @return 树形结果集
	 */
	@Override
	public List<Map<String, Object>> getTree(ID id) {
		List<Map<String, Object>> maps = getTree(id, false);
		getTree(maps);
		return maps;
	}

	/**
	 * 该方法根据上级编号查询本级数据或下级数据.为统一前端树形结构,需要将标识符,
	 * 如id全部转为treeId,显示的名称都改为treeName.,同时每次查询都需要将下级的数量查询出来,放入childNum字段中 {@link select b.dic_id
	 * treeId,b.dic_name treeName,b.dic_code dicCode, (select count(*) from td_dic a where a.pid =
	 * b.dic_Id) childNum from td_dic b}
	 * 
	 * @param id 条件编号
	 * @param parent 是否为上级菜单编号,true是,false否
	 * @return 树形结果集
	 */
	@Override
	public List<Map<String, Object>> getTree(ID id, boolean parent) {
		return null;
	}

	@SuppressWarnings("unchecked")
	private void getTree(List<Map<String, Object>> maps) {
		if (null == maps || maps.isEmpty()) {
			return;
		}
		for (Map<String, Object> map : maps) {
			if (Objects.nonNull(map.get("childNum")) && Integer.parseInt(map.get("childNum").toString()) > 0) {
				List<Map<String, Object>> childs = getTree((ID) (map.get("treeId").toString()), true);
				getTree(childs);
				map.put("children", childs);
			}
		}
	}

	@Override
	public boolean hasValue(Map<String, Object> param) {
		if (null == param || param.isEmpty()) {
			throw new ResultException("参数不能为空");
		}
		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			List<Predicate> cnds = new ArrayList<>();

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Set<Entry<String, Object>> entrySet = param.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					cnds.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
				}
				return criteriaBuilder.and(cnds.toArray(new Predicate[cnds.size()]));
			}
		};
		return baseRepository.count(spec) > 0;
	}

	@Override
	public boolean hasValue(String column, Object value) {
		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get(column), value);
			}
		};
		return baseRepository.count(spec) > 0;
	}

	@Override
	public T saveOrUpdate(T t) {
		return baseRepository.save(t);
	}

	/**
	 * 批量新增, 不带排序
	 * 
	 * @param ts 实体类参数列表
	 * @return 回显数据
	 */
	public List<T> saveOrUpdates(List<T> ts) {
		return baseRepository.saveAll(ts);
	}

	/**
	 * 全量字段更新表中数据,只有带readonly注解是字段不更新
	 * 
	 * @param model 需要更新的实体类参数
	 * @return 结果集,int或其他类型
	 */
	public Object update(T model) {
		return null;
	}
}