package com.wy.model;

import java.util.HashSet;
import java.util.Set;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表 ts_role
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 15:43:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "角色表 ts_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

	private Integer rid;

	private String rname;

	@Builder.Default
	private Set<Privilege> privileges = new HashSet<Privilege>();

	@Builder.Default
	private Set<Employee> employees = new HashSet<Employee>();
}