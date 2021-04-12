package com.wy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;

	private String username;

	private String password;

	private String realname;

	private Integer age;

	private Character sex;

	private Date birthday;

	private String address;

	private String email;

	private String idCard;

	private String telphone;

	private Role role;

	private UserEx userEx;

	private List<Role> roles;
}