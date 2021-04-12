package com.wy.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 专门来测试的实体类
 * 
 * @author ParadiseWY
 * @date 2020-09-27 20:20:56
 */
@Getter
@Setter
@ToString
public class Pojo {

	private Integer id;

	private String username;

	private char sex;

	private boolean flag;

	private double salary;

	private Date birthday;
}