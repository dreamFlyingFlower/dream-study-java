package com.wy.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wy.model.Role;
import com.wy.model.UserEx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;

	/**
	 * value:给字段起的别名,返回的时候有效
	 */
	@JsonProperty(value = "username")
	private String username;

	/**
	 * 忽略该字段,返回时不序列化
	 */
	@JsonIgnore
	private String password;

	private String realname;

	private Integer age;

	private Character sex;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;

	private String address;

	private String email;

	private String idCard;

	private String telphone;

	private Role role;

	private UserEx userEx;

	private List<Role> roles;
}