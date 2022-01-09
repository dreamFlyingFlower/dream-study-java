package com.wy.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.collection.MapTool;
import com.wy.common.Constants;
import com.wy.config.MessageService;
import com.wy.enums.ExecutorRouteStrategyEnum;
import com.wy.enums.MisfireStrategyEnum;
import com.wy.enums.ScheduleTypeEnum;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.model.XxlJobInfo;
import com.wy.properties.XxlJobProperties;
import com.wy.result.Result;
import com.wy.result.ResultException;
import com.wy.service.XxlJobInfoService;
import com.wy.service.XxlJobService;
import com.wy.util.CronExpression;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

/**
 * xxljob远程调用实现类.该类不提供生产前端调用,只提供测试调用
 *
 * @author 飞花梦影
 * @date 2022-01-04 17:32:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class XxlJobInfoServiceImpl implements XxlJobInfoService {

	@Autowired
	private XxlJobProperties xxlJobProperties;

	@Autowired
	private XxlJobService xxlJobService;

	/**
	 * 构建对象默认值
	 * 
	 * @param appName 执行器标识
	 * @return 默认值对象
	 */
	private XxlJobInfo generateJobInfo(String appName) {
		return XxlJobInfo.builder().author(xxlJobProperties.getUsername()).appName(xxlJobService.getAppName(appName))
				.scheduleType(xxlJobProperties.getJobInfo().getScheduleType().name())
				.glueType(xxlJobProperties.getJobInfo().getGlueType().name())
				.misfireStrategy(xxlJobProperties.getJobInfo().getMisfireStrategy().name())
				.executorBlockStrategy(xxlJobProperties.getJobInfo().getExecutorBlockStrategy().name())
				.executorRouteStrategy(xxlJobProperties.getJobInfo().getExecutorRouteStrategy().name()).build();
	}

	private void checkParam(XxlJobInfo jobInfo) {

		if (StrTool.isBlank(jobInfo.getJobDesc())) {
			throw new ResultException(MessageService.getMessage("system_please_input")
					+ MessageService.getMessage("jobinfo_field_jobdesc"));
		}
		if (StrTool.isBlank(jobInfo.getAuthor())) {
			throw new ResultException(MessageService.getMessage("system_please_input")
					+ MessageService.getMessage("jobinfo_field_author"));
		}

		// valid trigger
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
		Optional.ofNullable(scheduleTypeEnum).orElseThrow(() -> new ResultException(
				MessageService.getMessage("schedule_type") + MessageService.getMessage("system_unvalid")));
		if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
			if (jobInfo.getScheduleConf() == null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
				throw new ResultException("Cron " + MessageService.getMessage("system_unvalid"));
			}
		} else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE/* || scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY */) {
			if (jobInfo.getScheduleConf() == null) {
				throw new ResultException(
						MessageService.getMessage("schedule_type") + MessageService.getMessage("schedule_type"));
			}
			try {
				int fixSecond = Integer.valueOf(jobInfo.getScheduleConf());
				if (fixSecond < 1) {
					throw new ResultException(
							MessageService.getMessage("schedule_type") + MessageService.getMessage("system_unvalid"));
				}
			} catch (Exception e) {
				throw new ResultException(
						MessageService.getMessage("schedule_type") + MessageService.getMessage("system_unvalid"));
			}
		}

		// valid job
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			throw new ResultException(
					MessageService.getMessage("jobinfo_field_gluetype") + MessageService.getMessage("system_unvalid"));
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())
				&& (jobInfo.getExecutorHandler() == null || jobInfo.getExecutorHandler().trim().length() == 0)) {
			throw new ResultException(MessageService.getMessage("system_please_input") + " JobHandler");
		}
		// fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL == GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource() != null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
		}

		// valid advanced
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			throw new ResultException(MessageService.getMessage("jobinfo_field_executorRouteStrategy")
					+ MessageService.getMessage("system_unvalid"));
		}
		if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
			throw new ResultException(
					MessageService.getMessage("misfire_strategy") + MessageService.getMessage("system_unvalid"));
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			throw new ResultException(MessageService.getMessage("jobinfo_field_executorBlockStrategy")
					+ MessageService.getMessage("system_unvalid"));
		}
	}

	/**
	 * 新增定时任务,会重复新增相同的定时任务,即会有多个executorHandler相同的任务
	 * 
	 * @param xxlJobInfo 定时任务信息.基本类型包装类必须传值,否则在接收参数时会造成类型匹配错误
	 */
	@Override
	public Integer add(XxlJobInfo xxlJobInfo) {
		Optional.of(xxlJobInfo);
		XxlJobInfo jobInfo = generateJobInfo(xxlJobInfo.getAppName());
		BeanUtils.copyProperties(xxlJobInfo, jobInfo);
		checkParam(jobInfo);
		String content = xxlJobService.postObject(Constants.XXLJOB_URL_API_ADD, jobInfo);
		if (StrTool.isNotBlank(content) && NumberTool.isNumber(content)) {
			return Integer.parseInt(content);
		}
		return 0;
	}

	@Override
	public Result<?> remove(Integer id) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_REMOVE, MapTool.builder("id", id).build());
		return Result.ok();
	}

	@Override
	public Result<?> removeByName(String appName, String executorHandler) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_REMOVE_BY_NAME, MapTool
				.builder("appName", xxlJobService.getAppName(appName)).put("executorHandler", executorHandler).build());
		return Result.ok();
	}

	@Override
	public Result<?> start(Integer id) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_START, MapTool.builder("id", id).build());
		return Result.ok();
	}

	@Override
	public Result<?> startByName(String appName, String executorHandler) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_START_BY_NAME, MapTool
				.builder("appName", xxlJobService.getAppName(appName)).put("executorHandler", executorHandler).build());
		return Result.ok();
	}

	@Override
	public Result<?> stop(Integer id) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_STOP, MapTool.builder("id", id).build());
		return Result.ok();
	}

	@Override
	public Result<?> stopByName(String appName, String executorHandler) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_STOP_BY_NAME, MapTool
				.builder("appName", xxlJobService.getAppName(appName)).put("executorHandler", executorHandler).build());
		return Result.ok();
	}

	@Override
	public Result<?> trigger(Integer id, String executorParam, String addressList) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_TRIGGER,
				MapTool.builder("id", id).put("executorParam", executorParam).put("addressList", addressList).build());
		return Result.ok();
	}

	@Override
	public Result<?> triggerByName(String appName, String executorHandler, String executorParam, String addressList) {
		xxlJobService.getString(Constants.XXLJOB_URL_API_TRIGGER_BY_NAME,
				MapTool.builder("appName", xxlJobService.getAppName(appName)).put("executorHandler", executorHandler)
						.put("executorParam", executorParam).put("addressList", addressList).build());
		return Result.ok();
	}

	@Override
	public Integer update(XxlJobInfo xxlJobInfo) {
		Optional.of(xxlJobInfo);
		XxlJobInfo jobInfo = generateJobInfo(xxlJobInfo.getAppName());
		BeanUtils.copyProperties(xxlJobInfo, jobInfo);
		checkParam(jobInfo);
		String content = xxlJobService.postObject(Constants.XXLJOB_URL_API_UPDATE, jobInfo);
		if (StrTool.isNotBlank(content) && NumberTool.isNumber(content)) {
			return Integer.parseInt(content);
		}
		return 0;
	}
}