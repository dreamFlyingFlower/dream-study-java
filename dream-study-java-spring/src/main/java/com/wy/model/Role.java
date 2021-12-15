package com.wy.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色测试表,mybatis
 * 
 * @author ParadiseWY
 * @date 2019年8月26日
 */
@Data
public class Role extends Pojo{

	private Long roleId;

	private String roleName;
}