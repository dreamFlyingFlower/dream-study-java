package com.wy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerVo {

	private String id;

	private String name;

	private String gender;

	private String cellphone;

	private String qq;

	private String email;

	private String address;

	private String customerStatus;

	private String infoSource;

	private String message;

	private String intentionCourse;
}