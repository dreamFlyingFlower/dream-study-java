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
 * 资源查询
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
@Schema(description = "资源查询")
public class ResourceQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "资源名称")
	private String resourceName;

	@Schema(description = "资源类型")
	private String resourceType;

	@Schema(description = "资源地址")
	private String resourceUrl;

	@Schema(description = "权限")
	private String permission;

	@Schema(description = "上级资源ID")
	private Long parentId;

	@Schema(description = "上级名称")
	private String parentName;

	@Schema(description = "APP ID")
	private String appId;

	@Schema(description = "动作")
	private String resourceAction;

	@Schema(description = "图标")
	private String resourceIcon;

	@Schema(description = "样式")
	private String resourceStyle;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "状态")
	private Integer status;
}