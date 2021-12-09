package com.wy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限类
 *
 * @author 飞花梦影
 * @date 2021-04-17 17:23:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {

	private static final long serialVersionUID = 1l;

	private Integer pid;

	private String name;

	private String url;
}