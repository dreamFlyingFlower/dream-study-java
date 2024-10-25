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
 * 角色权限查询
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
@Schema(description = "角色权限查询")
public class RolePrivilegeQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "APP ID")
	private String appId;

	@Schema(description = "角色ID")
	private String roleId;

	@Schema(description = "资源ID")
	private String resourceId;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "状态")
	private Integer status;
}