package dream.study.authorization.server.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色权限")
public class RolePrivilegeVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "APP ID")
	@Size(max = 50, message = "APP ID最大长度不能超过50", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "角色ID")
	private String roleId;

	@Schema(description = "资源ID")
	@Size(max = 50, message = "资源ID最大长度不能超过50", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceId;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;
}