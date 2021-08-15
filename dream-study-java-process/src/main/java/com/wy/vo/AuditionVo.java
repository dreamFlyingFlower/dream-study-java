package com.wy.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditionVo {

	private String taskId;

	private String aid;

	private Date interviewTime;

	/**
	 * 意见
	 */
	private String comment;

	/**
	 * 得分
	 */
	private Double score;

	/**
	 * 是否通过
	 */
	private Boolean agree;
}