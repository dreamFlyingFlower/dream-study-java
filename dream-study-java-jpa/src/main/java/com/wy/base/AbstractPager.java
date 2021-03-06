package com.wy.base;

import java.io.Serializable;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wy.common.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 公用分页参数
 * 
 * @author 飞花梦影
 * @date 2021-01-06 22:36:25
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "分页参数,所有实体类和分页参数类都可继承该类")
@Getter
@Setter
public class AbstractPager implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 分页,从第几页开始查询 */
	@ApiModelProperty("分页,从第几页开始查询")
	@Transient
	@JsonIgnore
	private Integer pageIndex;

	/** 分页,每页查询数据条数 */
	@ApiModelProperty("分页,每页查询数据条数")
	@Transient
	@JsonIgnore
	private Integer pageSize;

	/** 排序字段,多个用逗号隔开 */
	@ApiModelProperty("排序字段,多个用逗号隔开")
	@Transient
	@JsonIgnore
	private String pageOrder;

	/** 升序或降序,升级asc,降序desc */
	@ApiModelProperty("升序或降序,升级asc,降序desc")
	@Transient
	@JsonIgnore
	private String pageDirection;

	/** 查询创建日期的开始时间 */
	@ApiModelProperty("查询创建日期的开始时间")
	@Transient
	@JsonIgnore
	private String beginCreatetime;

	/** 查询创建日期的结束时间 */
	@ApiModelProperty("查询创建日期的结束时间")
	@Transient
	@JsonIgnore
	private String endCreatetime;

	/** 查询更新时间的开始时间 */
	@ApiModelProperty("查询更新时间的开始时间")
	@Transient
	@JsonIgnore
	private String beginUpdatetime;

	/** 查询更新时间的结束时间 */
	@ApiModelProperty("查询更新时间的结束时间")
	@Transient
	@JsonIgnore
	private String endUpdatetime;

	/**
	 * 是否分页,当pageIndex不存在或小于0时,不分页;默认每页显示10条数据
	 * 
	 * @return true分页,false不分页
	 */
	public boolean hasPager() {
		if (pageIndex == null || pageIndex <= 0) {
			return false;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = Constants.PAGE_SIZE;
		}
		return true;
	}
}