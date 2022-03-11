package com.wy.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.wy.collection.ListTool;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 树形结构数据,若需要额外的参数,可继承本类
 * 
 * @author ParadiseWY
 * @date 2020年6月16日 上午11:16:40
 */
@Getter
@Setter
@ToString
public class Tree<T, ID> extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 树形结果标识符,该字段不可重复
	 */
	private ID treeId;

	/**
	 * 树形结构上级标识符
	 */
	private ID treePid;

	/**
	 * 树形结构需要展示的名称
	 */
	private String treeName;

	/**
	 * 树形结构中的扩展数据
	 */
	private Map<String, Object> extra;

	/**
	 * 树形结构本层数据的下级数据个数
	 */
	private Long childNum;

	/**
	 * 下层数据列表
	 */
	private List<T> children;

	/**
	 * 将列表转成树形结构,但是
	 * 
	 * @param <T>
	 * @param <ID>
	 * @param datas
	 * @return
	 */
	public static <T extends Tree<T, ID>, ID> List<Tree<T, ID>> transList2Tree(List<T> datas) {
		Map<ID, Tree<T, ID>> map = new HashMap<>();
		datas.forEach(t -> {
			map.put(t.getTreeId(), t);
		});
		List<Tree<T, ID>> rets = new ArrayList<>();
		datas.forEach(t -> {
			Tree<T, ID> parentTree = map.get(t.getTreePid());
			if (Objects.isNull(parentTree)) {
				rets.add(parentTree);
			} else {
				List<T> children = parentTree.getChildren();
				if (ListTool.isEmpty(children)) {
					children = new ArrayList<T>();
					parentTree.setChildren(children);
				}
				children.add(t);
			}
		});
		return rets;
	}
}