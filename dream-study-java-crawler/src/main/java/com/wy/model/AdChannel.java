package com.wy.model;

import java.util.Date;

import lombok.Data;

@Data
public class AdChannel {

	private Integer id;

	private String name;

	private String description;

	private Boolean isDefault;

	private Boolean status;

	private Byte ord;

	private Date createdTime;
}