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
 * 资源
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
@TableName("auth_resource")
public class ResourceEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 资源名称
	 */
	private String resourceName;

	/**
	 * 资源类型
	 */
	private String resourceType;

	/**
	 * 资源地址
	 */
	private String resourceUrl;

	/**
	 * 权限
	 */
	private String permission;

	/**
	 * 上级资源ID
	 */
	private String parentId;

	/**
	 * 上级名称
	 */
	private String parentName;

	/**
	 * APP ID
	 */
	private String appId;

	/**
	 * 动作
	 */
	private String resourceAction;

	/**
	 * 图标
	 */
	private String resourceIcon;

	/**
	 * 样式
	 */
	private String resourceStyle;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 排序
	 */
	private Integer sortIndex;

	/**
	 * 状态
	 */
	private Integer status;
}