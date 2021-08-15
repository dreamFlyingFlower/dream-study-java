package com.wy.model;

import java.util.HashSet;
import java.util.Set;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限表 tb_privilege
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 15:36:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "权限表 tb_privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Privilege {

	private Integer pid;

	private String modelName;

	private String privilegeName;

	@Builder.Default
	private Set<Role> roles = new HashSet<Role>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelName == null) ? 0 : modelName.hashCode());
		result = prime * result + ((privilegeName == null) ? 0 : privilegeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Privilege other = (Privilege) obj;
		if (modelName == null) {
			if (other.modelName != null)
				return false;
		} else if (!modelName.equals(other.modelName))
			return false;
		if (privilegeName == null) {
			if (other.privilegeName != null)
				return false;
		} else if (!privilegeName.equals(other.privilegeName))
			return false;
		return true;
	}
}