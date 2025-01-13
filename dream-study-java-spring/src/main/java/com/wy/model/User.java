package com.wy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wy.config.UserSerializer;

import dream.study.common.model.Role;
import dream.study.common.model.UserEx;
import lombok.Getter;
import lombok.Setter;

/**
 * 常用json注解
 * 
 * {@link JsonIgnore}:忽略单个字段,不序列化<br>
 * {@link JsonSerialize}:自定义序列化转换器,详见{@link UserSerializer}<br>
 * {@link JsonIgnoreProperties}:忽略多个字段,不序列化<br>
 * {@link JsonFormat}:只能在Date上使用,序列化日期格式
 * 
 * @author 飞花梦影
 * @date 2021-01-08 10:41:14
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
// @JsonSerialize(using = UserSerializer.class)
@JsonIgnoreProperties({ "password", "realname" })
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

	private Date createTime;

	private Role role;

	private UserEx userEx;

	private List<Role> roles;
}