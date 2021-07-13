package com.wy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统角色表ts_role
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:25:25
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Role implements Serializable {

	private static final long serialVersionUID = -5992084745763100751L;

	/**
	 * 角色编号
	 */
	private Long roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色类型:1超级管理员,2普通管理员,默认3普通用户
	 */
	private Integer roleType;

	/**
	 * 角色行为:默认1默认角色,NULL或其他值为非默认角色.该参数针对多角色时,默认登录使用的角色
	 */
	private Integer roleAction;

	/**
	 * 角色描述
	 */
	private String roleDesc;
}