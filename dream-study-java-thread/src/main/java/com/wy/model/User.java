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
 * 测试用实体类
 * 
 * @author ParadiseWY
 * @date 2020-11-28 10:15:11
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User implements Serializable {

	private static final long serialVersionUID = 8004730904191036222L;

	private Integer userId;

	private String username;

	private Integer age;

	private Double salary;

	private Date birthday;
}