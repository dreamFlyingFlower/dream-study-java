package com.wy.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Course实体类
 *
 * @author 飞花梦影
 * @date 2022-08-03 16:23:09
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course implements Serializable {

	private static final long serialVersionUID = -4130562097704924632L;

	private String id;

	private String name;

	private String users;

	private String mt;

	private String st;

	private String grade;

	private String studymodel;

	private String teachmode;

	private String description;

	private String pic;

	private Date timestamp;

	private String charge;

	private String valid;

	private String qq;

	private Double price;

	private Double price_old;

	private String expires;

	/**
	 * 课程计划
	 */
	private String teachplan;

	/**
	 * 课程发布时间
	 */
	private String pubTime;
}