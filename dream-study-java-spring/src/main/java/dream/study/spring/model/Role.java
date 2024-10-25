package dream.study.spring.model;

import dream.study.common.model.Pojo;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色测试表,mybatis
 * 
 * @author ParadiseWY
 * @date 2019年8月26日
 */
@Getter
@Setter
public class Role extends Pojo {

	private Long roleId;

	private String roleName;
}