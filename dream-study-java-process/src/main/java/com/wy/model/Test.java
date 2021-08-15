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
 * 测试题表 tb_test
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 15:44:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "测试题表 tb_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

	private String tid;

	private String title; // 标题 如：xxx__课程名称_基础测试

	@Builder.Default
	private Date uptime = new Date(); // 上传时间

	private String resumePath; // 保存地址

	private String message; // 用户留言

	private String comment; // 审批意见

	@Builder.Default
	private Boolean agree = false; // 是否通过

	private Date approveTime; // 审批时间

	private String employeeId;

	private String applyId;

	@Builder.Default
	private String status = Const.TEST_RUNTIME; // 状态

	@Builder.Default
	private Boolean close = false;

	private Double score;// 基础测试得分

	@TableField(exist = false)
	private Employee employee; // 审批人

	@TableField(exist = false)
	private Apply apply; // 属于哪个申请
}