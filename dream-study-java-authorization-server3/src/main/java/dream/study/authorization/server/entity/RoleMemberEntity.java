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
 * 角色成员
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
@TableName("auth_role_member")
public class RoleMemberEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 3385533803516065007L;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 成员ID
	 */
	private String memberId;

	/**
	 * 成员类型:用户或角色
	 */
	private String type;

	/**
	 * 机构ID
	 */
	private String instId;
}