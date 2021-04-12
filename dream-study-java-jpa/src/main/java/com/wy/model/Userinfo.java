package com.wy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户扩展信息
 * 
 * @author 飞花梦影
 * @date 2021-01-05 23:52:22
 * @git {@link https://github.com/mygodness100}
 */
@Table(name = "ti_userinfo")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Userinfo {

	@Id
	private Long userId;

	@Column
	private String qq;

	@Column
	private String weixin;
	
	/**
	 * 
	 */
	@OneToOne(mappedBy = "userinfo")
	private User user;
}