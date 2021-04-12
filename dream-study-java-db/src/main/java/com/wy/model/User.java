package com.wy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "ti_user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(length = 32, nullable = false)
	@Length(max = 32, message = "用户名长度不能超过32")
	private String username;

	/**
	 * 序列化默认使用jackson,若是要使用fastjson,需要配置.此处配置序列化的时候,不序列化密码
	 */
	@Column(length = 32, nullable = false)
	@JSONField(serialize = false)
	private String password;

	@Column(length = 16)
	private String realname;

	@Column
	private Integer age;

	@Column(length = 1)
	private Character sex;

	@Column
	private Date birthday;

	@Column(length = 64)
	private String address;

	@Column(length = 32)
	private String email;

	@Column(length = 32)
	private String idCard;

	@Column(length = 16)
	private String telphone;

	@Column
	private Integer state;

	private Role role;

	private UserEx userEx;

	private List<Role> roles;
}