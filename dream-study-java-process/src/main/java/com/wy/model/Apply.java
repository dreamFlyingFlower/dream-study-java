package com.wy.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.wy.common.Const;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户申请表 tb_apply
 * 
 * @auther 飞花梦影
 * @date 2021-08-14 14:57:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户申请表 tb_apply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apply {

	private String aid;

	private String title;

	private String customerId;

	@Builder.Default
	private String courseName = Const.JAVA_YUN;// 意向课程

	/**
	 * 意向班级
	 */
	@ApiModelProperty("意向班级")
	private String className;

	@Builder.Default
	private Date applyTime = new Date();// 申请时间

	@Builder.Default
	private String status = Const.SUBMIT_RESUME;// 入学申请的状态，用常量表示

	@Builder.Default
	private Double score = 0d; // 申请得分

	@Builder.Default
	private Double extraScore = 0d;// 其它加分

	@Builder.Default
	private Boolean close = false; // 申请是否结束

	@Builder.Default
	private Boolean success = false; // 申请是否成功

	private Date lastAccessTime; // 最后访问时间

	private String pi; // 和这个申请相关联的流程实例id

	private Customer customer;

	@Builder.Default
	private Set<Resume> resumes = new HashSet<Resume>(); // 用户提交的自荐信

	@Builder.Default
	private Set<Test> tests = new HashSet<Test>(); // 用户提交的入学测试题

	@Builder.Default
	private Set<Qualification> qualifications = new HashSet<Qualification>(); // 用户提交的入学考试资格

	@Builder.Default
	private Set<Exam> exams = new HashSet<Exam>(); // 用户提交的入学考试

	@Builder.Default
	private Set<Audition> auditions = new HashSet<Audition>(); // 用户面试

	/**
	 * 总得分
	 * 
	 * @return
	 */
	public Double getSumScore() {
		return score + extraScore;
	}
}