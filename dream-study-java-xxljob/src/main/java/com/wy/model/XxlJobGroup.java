package com.wy.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 执行器分组表 xxl_job_group
 * 
 * @auther 飞花梦影
 * @date 2022-01-08 00:38:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "执行器分组表 xxl_job_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XxlJobGroup {

	/**
	 * 执行器主键ID
	 */
	@ApiModelProperty("执行器主键ID")
	private int id;

	/**
	 * 执行器标识,唯一
	 */
	@ApiModelProperty("执行器标识,唯一")
	private String appname;

	/**
	 * 执行器名
	 */
	@ApiModelProperty("执行器名")
	private String title;

	/**
	 * 执行器地址类型:0->自动注册,1->手动录入
	 */
	@ApiModelProperty("执行器地址类型:0->自动注册,1->手动录入")
	private int addressType;

	/**
	 * 执行器地址列表,多地址逗号分隔(手动录入)
	 */
	@ApiModelProperty("执行器地址列表,多地址逗号分隔(手动录入)")
	private String addressList;

	/**
	 * 执行器更新时间
	 */
	@ApiModelProperty("执行器更新时间")
	private Date updateTime;

	/**
	 * 执行器地址列表(系统注册)
	 */
	@ApiModelProperty("执行器地址列表(系统注册)")
	private List<String> registryList;

	public List<String> getRegistryList() {
		if (addressList != null && addressList.trim().length() > 0) {
			registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
		}
		return registryList;
	}
}