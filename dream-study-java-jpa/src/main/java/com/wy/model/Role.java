package com.wy.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色
 *
 * @author 飞花梦影
 * @date 2019-08-26 23:20:49
 * @git {@link https://github.com/mygodness100}
 */
@Entity
@Table(name = "tb_role")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "role_name")
	private String roleName;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users;
}