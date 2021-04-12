package com.wy.model;

import java.util.Date;

import lombok.Data;

@Data
public class AdLabel {

	private Integer id;

	private String name;

	private Boolean type;

	private Date createdTime;
}