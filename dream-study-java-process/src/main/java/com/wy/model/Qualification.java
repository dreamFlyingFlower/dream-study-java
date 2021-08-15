package com.wy.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wy.common.Const;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 入学考试资格 tb_qualification
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 15:36:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "入学考试资格 tb_qualification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Qualification {

	private String taskId;

	private String qid;

	private String title; // 标题 如：xxx__课程名称_入学考试资格

	@Builder.Default
	private Date uptime = new Date(); // 上传时间

	private String resumePath; // 保存地址

	private String message; // 用户留言

	private String comment; // 审批意见

	@Builder.Default
	private Boolean agree = false; // 是否通过

	private Date approveTime; // 审批时间

	@Builder.Default
	private String status = Const.ENTRANCE_EXAM_QUALIFICATION_PASS; // 状态

	@Builder.Default
	private Boolean close = false;

	private Double score;// 入学考试得分

	private String applyId;

	private String employeeId;

	@TableField(exist = false)
	private Apply apply; // 属于哪个申请

	@TableField(exist = false)
	private Employee employee; // 审批人
}