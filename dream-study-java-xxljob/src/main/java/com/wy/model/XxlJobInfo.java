package com.wy.model;

import java.util.Date;

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

	private Integer id; // 主键ID

	private Integer jobGroup; // 执行器主键ID

	private String jobDesc;

	private Date addTime;

	private Date updateTime;

	private String author; // 负责人

	private String alarmEmail; // 报警邮件

	private String scheduleType; // 调度类型

	private String scheduleConf; // 调度配置,值含义取决于调度类型

	private String misfireStrategy; // 调度过期策略

	private String executorRouteStrategy; // 执行器路由策略

	private String executorHandler; // 执行器,任务Handler名称

	private String executorParam; // 执行器,任务参数

	private String executorBlockStrategy; // 阻塞处理策略

	private Integer executorTimeout; // 任务执行超时时间,单位秒

	private Integer executorFailRetryCount; // 失败重试次数

	private String glueType; // GLUE类型 #com.xxl.job.core.glue.GlueTypeEnum

	private String glueSource; // GLUE源代码

	private String glueRemark; // GLUE备注

	private Date glueUpdatetime; // GLUE更新时间

	private String childJobId; // 子任务ID,多个逗号分隔

	private Integer triggerStatus; // 调度状态：0-停止,1-运行

	private Long triggerLastTime; // 上次调度时间

	private Long triggerNextTime; // 下次调度时间
}