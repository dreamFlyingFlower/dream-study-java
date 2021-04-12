package com.wy.model;

import java.io.Serializable;
import java.util.Date;

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

	private Integer age;

	private Date birthday;
}