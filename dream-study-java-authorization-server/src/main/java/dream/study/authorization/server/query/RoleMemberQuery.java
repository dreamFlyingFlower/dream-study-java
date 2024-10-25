package dream.study.authorization.server.query;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 角色成员查询
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
@Schema(description = "角色成员查询")
public class RoleMemberQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "角色ID")
	private String roleId;

	@Schema(description = "成员ID")
	private String memberId;

	@Schema(description = "成员类型")
	private String type;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "角色名称")
	private String roleName;

	@Schema(defaultValue = "用户名")
	private String username;

	@Schema(description = "用户显示名")
	private String displayName;
}