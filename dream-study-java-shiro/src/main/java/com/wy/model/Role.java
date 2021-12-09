package com.wy.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表
 * 
 * @author 飞花梦影
 * @date 2019-08-26 09:33:41
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	private Long roleId;

	private String roleName;

	private Set<Permission> permissions;

	private Set<User> users;
}