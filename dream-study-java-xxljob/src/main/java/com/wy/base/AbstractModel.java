package com.wy.base;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wy.database.BaseModelField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 实体类基类,可继承不可不继承
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:55:25
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel("实体类基类,可继承不可不继承")
@Getter
@Setter
public abstract class AbstractModel extends AbstractPager {

	private static final long serialVersionUID = 1L;

	/** 创建者 */
	@ApiModelProperty("创建者,可为null")
	@BaseModelField
	private Long creater;

	/** 创建时间,格式为yyyy-MM-dd HH:mm:ss */
	@ApiModelProperty("创建时间,可为null")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@BaseModelField
	private Date createtime;

	/** 更新者 */
	@ApiModelProperty("更新者,可为null")
	@BaseModelField
	private Long updater;

	/** 更新时间,格式为yyyy-MM-dd HH:mm:ss */
	@ApiModelProperty("更新时间,可为null")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@BaseModelField
	private Date updatetime;

	@ApiModelProperty("备注")
	@BaseModelField
	private String remark;

	/** 数据权限 */
	@ApiModelProperty("数据权限")
	@BaseModelField
	private String permission;

	/** 请求参数 */
	@ApiModelProperty("请求参数")
	private Map<String, Object> params;
}