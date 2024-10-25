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
 * 资源
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源")
public class ResourceVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "资源名称")
	@Size(max = 32, message = "资源名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceName;

	@Schema(description = "资源类型")
	@Size(max = 32, message = "资源类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceType;

	@Schema(description = "资源地址")
	@Size(max = 64, message = "资源地址最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceUrl;

	@Schema(description = "权限")
	@Size(max = 256, message = "权限最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String permission;

	@Schema(description = "上级资源ID")
	private Long parentId;

	@Schema(description = "上级名称")
	@Size(max = 64, message = "上级名称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String parentName;

	@Schema(description = "APP ID")
	@Size(max = 64, message = "APP ID最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appId;

	@Schema(description = "动作")
	@Size(max = 32, message = "动作最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceAction;

	@Schema(description = "图标")
	@Size(max = 32, message = "图标最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceIcon;

	@Schema(description = "样式")
	@Size(max = 256, message = "样式最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String resourceStyle;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "排序")
	private Integer sortIndex;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "机构名称")
	private String instName;

	@Schema(description = "应用名称")
	private String appName;
}