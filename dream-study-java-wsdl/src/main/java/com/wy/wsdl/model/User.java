package com.wy.wsdl.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer userId;

	private String username;

	private String password;

	private String realname;

	private Integer departId;

	private String idcard;

	private Date birthday;

	private Integer age;

	private Character sex;

	private String address;

	private String email;

	private String tel;

	private String salary;

	private Integer state;

	private String userIcon;

	private Date createtime;

	private Date updatetime;
}