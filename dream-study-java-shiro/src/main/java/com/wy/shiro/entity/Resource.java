package com.wy.shiro.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源表
 * 
 * @auther 飞花梦影
 * @date 2022-06-22 00:15:05
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@ApiModel(description = "资源表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 父资源
	 */
	private String parentId;

	/**
	 * 资源名称
	 */
	private String resourceName;

	/**
	 * 资源路径
	 */
	private String requestPath;

	/**
	 * 资源标签
	 */
	private String label;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 是否叶子节点
	 */
	private String isLeaf;

	/**
	 * 资源类型
	 */
	private String resourceType;

	/**
	 * 排序
	 */
	private Integer sortNo;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 系统code
	 */
	private String systemCode;

	/**
	 * 是否根节点
	 */
	private String isSystemRoot;

	/**
	 * 是否有效
	 */
	private String enableFlag;
}