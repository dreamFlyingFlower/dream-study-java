package com.wy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualificationVo {

	private String taskId;

	private String qid;

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