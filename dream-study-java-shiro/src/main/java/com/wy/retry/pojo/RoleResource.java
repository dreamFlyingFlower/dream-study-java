package com.wy.retry.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String enableFlag;

	private String roleId;

	private String resourceId;

}