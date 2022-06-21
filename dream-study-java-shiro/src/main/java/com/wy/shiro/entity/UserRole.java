package com.wy.shiro.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String enableFlag;

	private String userId;

	private String roleId;

}