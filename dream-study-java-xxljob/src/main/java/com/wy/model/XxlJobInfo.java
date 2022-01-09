package com.wy.model;

import java.util.Date;

import com.xxl.job.core.glue.GlueTypeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定时任务实体类
 *
 * @author 飞花梦影
 * @date 2022-01-05 14:10:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XxlJobInfo {

	/**
	 * 定时任务信息主键ID
	 */
	@ApiModelProperty("定时任务信息主键ID")
	private Integer id;

	/**
	 * 执行器组主键ID
	 */
	@ApiModelProperty("执行器组主键ID")
	private Integer jobGroup;

	/**
	 * 执行器标识,唯一
	 */
	@ApiModelProperty("执行器标识,唯一")
	private String appName;

	/**
	 * 定时任务描述
	 */
	@ApiModelProperty("定时任务描述")
	private String jobDesc;

	/**
	 * 定时器新增时间
	 */
	@ApiModelProperty("定时器新增时间")
	private Date addTime;

	/**
	 * 定时器更新时间
	 */
	@ApiModelProperty("定时器更新时间")
	private Date updateTime;

	/**
	 * 负责人
	 */
	@ApiModelProperty("负责人")
	private String author;

	/**
	 * 报警邮件
	 */
	@ApiModelProperty("报警邮件")
	private String alarmEmail;

	/**
	 * 调度类型
	 */
	@ApiModelProperty("调度类型")
	private String scheduleType;

	/**
	 * 调度配置,值含义取决于调度类型
	 */
	@ApiModelProperty("调度配置,值含义取决于调度类型")
	private String scheduleConf;

	/**
	 * 调度过期策略
	 */
	@ApiModelProperty("调度过期策略")
	private String misfireStrategy;

	/**
	 * 执行器路由策略
	 */
	@ApiModelProperty("执行器路由策略")
	private String executorRouteStrategy;

	/**
	 * 执行器,任务Handler名称
	 */
	@ApiModelProperty("执行器,任务Handler名称")
	private String executorHandler;

	/**
	 * 执行器,任务参数
	 */
	@ApiModelProperty("执行器,任务参数")
	private String executorParam;

	/**
	 * 阻塞处理策略
	 */
	@ApiModelProperty("阻塞处理策略")
	private String executorBlockStrategy;

	/**
	 * 任务执行超时时间,单位秒
	 */
	@ApiModelProperty("任务执行超时时间,单位秒")
	private Integer executorTimeout;

	/**
	 * 失败重试次数
	 */
	@ApiModelProperty("失败重试次数")
	private Integer executorFailRetryCount;

	/**
	 * GLUE类型 {@link GlueTypeEnum}
	 */
	@ApiModelProperty("GLUE类型")
	private String glueType;

	/**
	 * GLUE源代码
	 */
	@ApiModelProperty("GLUE源代码")
	private String glueSource;

	/**
	 * GLUE备注
	 */
	@ApiModelProperty("GLUE备注")
	private String glueRemark;

	/**
	 * GLUE更新时间
	 */
	@ApiModelProperty("GLUE更新时间")
	private Date glueUpdatetime;

	/**
	 * 子任务ID,多个逗号分隔
	 */
	@ApiModelProperty("子任务ID,多个逗号分隔")
	private String childJobId;

	/**
	 * 调度状态：0-停止,1-运行
	 */
	@ApiModelProperty("调度状态：0-停止,1-运行")
	private Integer triggerStatus;

	/**
	 * 上次调度时间
	 */
	@ApiModelProperty("上次调度时间")
	private Long triggerLastTime;

	/**
	 * 下次调度时间
	 */
	@ApiModelProperty("下次调度时间")
	private Long triggerNextTime;
}