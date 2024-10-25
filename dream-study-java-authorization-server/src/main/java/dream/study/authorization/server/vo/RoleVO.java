package dream.study.authorization.server.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
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
 * 角色
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色")
public class RoleVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "角色编码")
	@NotBlank(message = "角色编码不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "角色编码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String roleCode;

	@Schema(description = "角色名称")
	@NotBlank(message = "角色名称不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "角色名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String roleName;

	@Schema(description = "动态用户组,dynamic动态组 static静态组app应用账号组")
	@Size(max = 16, message = "动态用户组,dynamic动态组 static静态组app应用账号组最大长度不能超过16",
			groups = { ValidAdd.class, ValidEdit.class })
	private String category;

	@Schema(description = "过滤条件SQL")
	@Size(max = 65535, message = "过滤条件SQL最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String filters;

	@Schema(description = "机构列表")
	@Size(max = 65535, message = "机构列表最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String orgIdsList;

	@Schema(description = "恢复时间")
	@Size(max = 45, message = "恢复时间最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String resumeTime;

	@Schema(description = "暂停时间")
	@Size(max = 45, message = "暂停时间最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String suspendTime;

	@Schema(description = "是否默认")
	private Integer isDefault;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;
}