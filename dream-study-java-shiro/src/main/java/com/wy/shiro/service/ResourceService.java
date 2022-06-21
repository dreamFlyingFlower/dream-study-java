package com.wy.shiro.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.vo.TreeVo;

/**
 * 资源服务接口
 * 
 * @author 飞花梦影
 * @date 2022-06-22 00:06:54
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ResourceService extends IService<Resource> {

	/**
	 * 资源查询
	 * 
	 * @param resource 参数
	 * @param rows 分页页数
	 * @param page 分页条数
	 * @return 数据集合
	 */
	List<Resource> findResourceList(Resource resource, Integer rows, Integer page);

	/**
	 * 资源查询统计
	 * 
	 * @param resource 参数
	 * @return 统计数据
	 */
	long countResourceList(Resource resource);

	/**
	 * 主键查询资源
	 * 
	 * @param id
	 * @return
	 */
	Resource findOne(String id);

	/**
	 * 修改或修改资源对象
	 * 
	 * @param resource
	 */
	void saveOrUpdateResource(Resource resource);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	void deleteByids(List<String> ids);

	/**
	 * 按照标识查询资源
	 * 
	 * @param label
	 * @return
	 */
	String findByLabel(String label);

	/**
	 * 查找所有树按SortNo降序
	 * 
	 * @return
	 */
	List<TreeVo> findAllOrderBySortNoAsc();

	/**
	 * 查找所有树按SortNo降序,并初始化已选选项
	 * 
	 * @param resourceIds
	 * @return
	 */
	List<TreeVo> findAllOrderBySortNoAscChecked(String resourceIds);

	/**
	 * 按父Id查询树
	 * 
	 * @param parentId
	 * @return
	 */
	List<TreeVo> findResourceTreeVoByParentId(String parentId);

	/**
	 * 按资源标识删除资源
	 * 
	 * @param label
	 * @return
	 */
	boolean deleteByLabel(String label);
}