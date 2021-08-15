package com.wy.model;

import java.util.HashSet;
import java.util.Set;

import com.wy.base.AbstractPager;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工表 tb_employee
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 17:14:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "员工表 tb_employee")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends AbstractPager {

	private static final long serialVersionUID = 2566759171131757283L;

	private Integer eid;

	private String username;

	private String password;

	private String realname;

	private Department department;

	@Builder.Default
	private Set<Role> roles = new HashSet<Role>();
}