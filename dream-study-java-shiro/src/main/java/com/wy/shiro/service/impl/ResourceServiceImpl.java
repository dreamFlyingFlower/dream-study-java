package com.wy.shiro.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wy.lang.StrTool;
import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.RoleResource;
import com.wy.shiro.entity.RoleResourceExample;
import com.wy.shiro.entity.vo.TreeVo;
import com.wy.shiro.mapper.ResourceMapper;
import com.wy.shiro.mapper.ResourceServiceMapper;
import com.wy.shiro.mapper.RoleResourceMapper;
import com.wy.shiro.service.ResourceService;

/**
 * 资源服务实现类
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:11:57
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	@Autowired
	private ResourceServiceMapper resourceServiceMapper;

	@Autowired
	private RoleResourceMapper roleResourceMapper;

	@Override
	public List<Resource> findResourceList(Resource resource, Integer rows, Integer page) {
		LambdaQueryChainWrapper<Resource> queryChainWrapper = this.generateCondition(resource);
		return page(new Page<Resource>(page, rows), queryChainWrapper).getRecords();
	}

	@Override
	public long countResourceList(Resource resource) {
		LambdaQueryChainWrapper<Resource> queryChainWrapper = this.generateCondition(resource);
		return super.count(queryChainWrapper);
	}

	private LambdaQueryChainWrapper<Resource> generateCondition(Resource resource) {
		return this.lambdaQuery()
				.eq(StrTool.isNotBlank(resource.getParentId()), Resource::getParentId, resource.getParentId())
				.eq(Resource::getEnableFlag, SuperConstant.YES).ne(Resource::getParentId, "-1")
				.like(StrTool.isNotBlank(resource.getResourceName()), Resource::getResourceName,
						resource.getResourceName())
				.like(StrTool.isNotBlank(resource.getLabel()), Resource::getLabel, resource.getLabel())
				.like(StrTool.isNotBlank(resource.getRequestPath()), Resource::getRequestPath,
						resource.getRequestPath())
				.orderByAsc(Arrays.asList(Resource::getParentId, Resource::getSortNo));
	}

	@Override
	public Resource findOne(String id) {
		List<Resource> list = this.lambdaQuery().eq(Resource::getEnableFlag, SuperConstant.YES).list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveOrUpdateResource(Resource resource) {
		if (StrTool.isNotBlank(resource.getId())) {
			resourceMapper.updateById(resource);
		} else {
			if (StrTool.isBlank(resource.getIsSystemRoot())) {
				resource.setIsSystemRoot(SuperConstant.NO);
			}
			resource.setEnableFlag(SuperConstant.YES);
			resourceMapper.insert(resource);
		}
	}

	@Transactional
	@Override
	public void deleteByids(List<String> ids) {
		this.lambdaUpdate().set(Resource::getEnableFlag, SuperConstant.NO).in(Resource::getId, ids).update();
	}

	@Transactional
	@Override
	public boolean deleteByLabel(String label) {
		return remove(this.lambdaQuery().eq(Resource::getEnableFlag, SuperConstant.YES).eq(Resource::getLabel, label));
	}

	@Override
	public String findByLabel(String label) {
		List<Resource> list =
				this.lambdaQuery().eq(Resource::getEnableFlag, SuperConstant.YES).eq(Resource::getLabel, label).list();
		if (list.size() == 1) {
			return list.get(0).getLabel();
		}
		return null;
	}

	@Override
	public List<TreeVo> findAllOrderBySortNoAsc() {
		List<Resource> list = this.lambdaQuery().eq(Resource::getEnableFlag, SuperConstant.YES)
				.orderByAsc(Resource::getSortNo).list();
		List<TreeVo> listHandle = new ArrayList<>();
		for (Resource resource : list) {
			TreeVo treeVo = new TreeVo();
			treeVo.setId(resource.getId());
			treeVo.setpId(resource.getParentId());
			treeVo.setName(resource.getResourceName());
			treeVo.setOpen(Boolean.TRUE);
			listHandle.add(treeVo);
		}
		return listHandle;
	}

	@Override
	public List<TreeVo> findAllOrderBySortNoAscChecked(String resourceIds) {
		List<TreeVo> treeVoIterable = this.findAllOrderBySortNoAsc();
		String[] resourceId = resourceIds.split(",");
		for (String id : resourceId) {
			for (TreeVo treeVo : treeVoIterable) {
				treeVo.setOpen(Boolean.TRUE);
				if (treeVo.getId().equals(id)) {
					treeVo.setChecked(Boolean.TRUE);
				}
			}
		}
		return treeVoIterable;
	}

	@Override
	public List<TreeVo> findResourceTreeVoByParentId(String parentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Resource> list = new ArrayList<>();
		if (SuperConstant.ROOT_PARENT_ID.equals(parentId)) {
			map.put("isSystemRoot", SuperConstant.YES);
			map.put("enableFlag", SuperConstant.YES);
			list.addAll(resourceServiceMapper.findResourceTreeVoByParentId(map));
		} else {
			list.addAll(this.lambdaQuery().eq(Resource::getEnableFlag, SuperConstant.YES)
					.eq(Resource::getParentId, parentId).orderByAsc(Resource::getSortNo).list());
		}
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		for (Resource resources : list) {
			TreeVo treeVo = new TreeVo(resources.getId(), resources.getParentId(), resources.getResourceName());
			if (SuperConstant.ROOT_PARENT_ID.equals(parentId)) {
				treeVo.setOpen(Boolean.TRUE);
			}
			treeVoList.add(treeVo);
		}
		return treeVoList;
	}

	public List<String> findRoleHasResourceIds(List<String> roleIds) {
		RoleResourceExample roleResourceExample = new RoleResourceExample();
		roleResourceExample.createCriteria().andRoleIdIn(roleIds).andEnableFlagEqualTo(SuperConstant.YES);
		List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(roleResourceExample);
		List<String> list = new ArrayList<>();
		roleResourceList.forEach(n -> list.add(n.getResourceId()));
		return list;
	}
}