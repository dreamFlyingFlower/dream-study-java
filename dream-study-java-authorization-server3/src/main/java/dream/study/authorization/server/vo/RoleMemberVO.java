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
@Schema(description = "角色成员")
public class RoleMemberVO extends UserVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "角色ID")
	@NotNull(message = "角色ID不能为空", groups = { ValidAdd.class })
	private String roleId;

	@Schema(description = "成员ID")
	@NotNull(message = "成员ID不能为空", groups = { ValidAdd.class })
	private String memberId;

	@Schema(description = "成员类型")
	@NotBlank(message = "成员类型不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "成员类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String type;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "角色名称")
	private String roleName;

	@Schema(description = "分类")
	private String category;

	@Schema(description = "成员名称")
	private String memberName;

	public RoleMemberVO(String roleId, String roleName, String memberId, String memberName, String type,
			String instId) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.memberId = memberId;
		this.memberName = memberName;
		this.type = type;
		this.instId = instId;
	}
}