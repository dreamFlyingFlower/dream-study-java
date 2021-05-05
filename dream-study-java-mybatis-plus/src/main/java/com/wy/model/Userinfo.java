package com.wy.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wy.valid.ValidAdd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户信息扩展表 ts_userinfo
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "用户信息扩展表 ts_userinfo")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Userinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	@NotNull(groups = ValidAdd.class)
	private Long userId;

	/**
	 * 兴趣爱好
	 */
	@ApiModelProperty("兴趣爱好")
	private String interest;

	/**
	 * 学历,见ts_dict表EDUCATION
	 */
	@ApiModelProperty("学历,见ts_dict表EDUCATION")
	private Integer education;

	/**
	 * 毕业学校
	 */
	@ApiModelProperty("毕业学校")
	private String university;

	/**
	 * 专业
	 */
	@ApiModelProperty("专业")
	private String professional;

	/**
	 * 离职时间,格式为yyyy-MM-dd
	 */
	@ApiModelProperty("离职时间,格式为yyyy-MM-dd")
	private Date resigndate;

	/**
	 * 婚姻状态,见ts_dict表MARRY
	 */
	@ApiModelProperty("婚姻状态,见ts_dict表MARRY")
	private Integer marry;

	/**
	 * 身高,单位cm
	 */
	@ApiModelProperty("身高,单位cm")
	private Integer height;

	/**
	 * 体重,单位kg
	 */
	@ApiModelProperty("体重,单位kg")
	private Double weight;

	/**
	 * 血型,见ts_dict表BLOOD
	 */
	@ApiModelProperty("血型,见ts_dict表BLOOD")
	private String blood;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/** 非数据库字段 */
}