package com.wy.model;

import com.wy.base.AbstractPager;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 客户表 tb_customer
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 15:09:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "客户表 tb_customer")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends AbstractPager {

	private static final long serialVersionUID = 1883617311336300788L;

	private String id;

	private String name;

	private String gender;

	private String cellphone;

	private String qq;

	private String email;

	private String address;

	private String customerStatus;

	private String infoSource;

	private String intentionCourse;

	private String message;
}