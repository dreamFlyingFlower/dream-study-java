package com.wy.base;

import java.util.List;
import java.util.Map;

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
	 * 树形结果标识符,若该字段可能重复时,使用treeCode
	 */
	private ID treeId;

	/**
	 * 树形结构需要展示的名称
	 */
	private String treeName;

	/**
	 * 树形结构的Code
	 */
	private String treeCode;

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
}