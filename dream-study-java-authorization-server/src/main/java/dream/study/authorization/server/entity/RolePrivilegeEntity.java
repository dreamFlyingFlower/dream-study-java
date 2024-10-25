package dream.study.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("auth_role_privilege")
public class RolePrivilegeEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * APP ID
	 */
	private String appId;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 资源ID
	 */
	private String resourceId;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 状态
	 */
	private Integer status;
}