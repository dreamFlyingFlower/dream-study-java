package com.wy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 角色测试表,mybatis
 * 
 * @author ParadiseWY
 * @date 2019年8月26日
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	private Long roleId;

	private String roleName;

	private Set<Permission> permissions;

	private Set<User> users;
}