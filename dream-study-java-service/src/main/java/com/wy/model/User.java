package com.wy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户表ts_user
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:09:14
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements Serializable {

	private static final long serialVersionUID = 906113084965573919L;

	private Long userId;

	private String username;

	@JsonSerialize
	private String password;

	private String mobile;

	private String email;

	private Integer age;

	private Date birthday;

	private String gender;

	private Date createtime;

	private Date updatetime;

	private List<Role> roles;
}