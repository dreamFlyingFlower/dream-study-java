package com.wy.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
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
import com.wy.service.XxlJobService;
import com.wy.util.CronExpression;
import com.wy.util.RestTemplateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * xxljob远程调用实现类.该类不提供生产前端调用,只提供测试调用
 *
 * @author 飞花梦影
 * @date 2022-01-04 17:32:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
@Slf4j
public class XxlJobServiceImpl implements XxlJobService {

	private static final Map<String, String> USER_COOKIE = new HashMap<>();

	private ParameterizedTypeReference<ReturnT<String>> retType = new ParameterizedTypeReference<ReturnT<String>>() {
	};

	@Autowired
	private XxlJobProperties xxlJobProperties;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 从缓存中获取cookie,缓存中没有则调用登录接口获取
	 * 
	 * @return cookie值
	 */
	private String getCookie() {
		String cookie = USER_COOKIE.get(xxlJobProperties.getUsername());
		if (StrTool.isBlank(cookie)) {
			cookie = login();
			if (StrTool.isBlank(cookie)) {
				throw new ResultException("登录xxljob-admin失败,请联系管理员");
			}
		}
		return cookie;
	}

	/**
	 * 从登录的请求接口中获得cookie
	 * 
	 * @param responseEntity 登录响应结果
	 * @return cookie值
	 */
	private String getCookie(ResponseEntity<ReturnT<String>> responseEntity) {
		// Set-Cookie=[XXL_JOB_LOGIN_IDENTITY=cookie; Path=/; HttpOnly]
		String cookie = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
		int start = cookie.indexOf(Constants.XXLJOB_COOKIE_LOGIN_KEY) + 1 + Constants.XXLJOB_COOKIE_LOGIN_KEY.length();
		cookie = cookie.substring(start, cookie.indexOf(";", start));
		USER_COOKIE.put(xxlJobProperties.getUsername(), cookie);
		return cookie;
	}

	/**
	 * 构建带cookie的请求头
	 * 
	 * @return 请求头
	 */
	private HttpHeaders generateHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.COOKIE, Constants.XXLJOB_COOKIE_LOGIN_KEY + "=" + getCookie());
		return httpHeaders;
	}

	/**
	 * Get请求
	 * 
	 * @param api 接口地址
	 * @param params 参数
	 * @return 结果
	 */
	private String get(String api, Map<String, Object> params) {
		String url = RestTemplateUtil.generateGetUrl(xxlJobProperties.getAdminAddresses().split(",")[0] + api, params);
		ResponseEntity<ReturnT<String>> responseEntity =
				restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(generateHeader()), retType, params);
		return handlerResponse(responseEntity);
	}

	/**
	 * Post请求传输对象
	 * 
	 * @param <T>
	 * @param api 接口地址
	 * @param t 参数
	 * @return 结果
	 */
	private <T> String postObject(String api, T t) {
		ResponseEntity<ReturnT<String>> responseEntity =
				restTemplate.exchange(xxlJobProperties.getAdminAddresses().split(",")[0] + api, HttpMethod.POST,
						new HttpEntity<MultiValueMap<String, Object>>(RestTemplateUtil.toLinkedMultiValueMap(t),
								generateHeader()),
						retType);
		return handlerResponse(responseEntity);
	}

	/**
	 * 处理结果
	 * 
	 * @param responseEntity 响应
	 * @return 结果字符串
	 */
	private String handlerResponse(ResponseEntity<ReturnT<String>> responseEntity) {
		log.info("@@@:XxlJob请求结果:{}", JSON.toJSONString(responseEntity));
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			if (200 == responseEntity.getBody().getCode()) {
				return responseEntity.getBody().getContent();
			} else {
				throw new ResultException(responseEntity.getBody().getMsg());
			}
		} else {
			log.error("###:xxljob请求响应失败,错误码为:{}", responseEntity.getStatusCodeValue());
			throw new ResultException("###:xxljob请求响应失败,错误码为:%s", responseEntity.getStatusCodeValue());
		}
	}

	/**
	 * 新增定时任务,会重复新增相同的定时任务,即会有多个executorHandler相同的任务
	 * 
	 * @param jobInfo 定时任务信息
	 */
	@Override
	public Integer add(XxlJobInfo jobInfo) {

		// 测试数据
		jobInfo =
				XxlJobInfo.builder().jobGroup(1).jobDesc("test44").author("admin").scheduleType("CRON").glueType("BEAN")
						.executorRouteStrategy("FIRST").misfireStrategy("DO_NOTHING").scheduleConf("0 0/1 * * * ?")
						.executorHandler("demoJobHandler").executorBlockStrategy("SERIAL_EXECUTION").build();

		if (Objects.isNull(jobInfo.getJobGroup())) {
			throw new ResultException("定时任务JobGroup不能为空");
		}
		if (StrTool.isBlank(jobInfo.getJobDesc())) {
			throw new ResultException("定时任务JobDesc不能为空");
		}
		if (StrTool.isBlank(jobInfo.getAuthor())) {
			throw new ResultException("定时任务Author不能为空");
		}
		// valid trigger
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
		if (scheduleTypeEnum == null) {
			throw new ResultException(
					MessageService.getMessage("schedule_type") + MessageService.getMessage("system_unvalid"));
		}
		if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
			if (jobInfo.getScheduleConf() == null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
				throw new ResultException("Cron " + MessageService.getMessage("system_unvalid"));
			}
		} else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE/* || scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY */) {
			if (jobInfo.getScheduleConf() == null) {
				throw new ResultException("Cron " + MessageService.getMessage("schedule_type"));
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

		String content = postObject(Constants.XXLJOB_URL_API_ADD, jobInfo);
		if (StrTool.isNotBlank(content) && NumberTool.isNumber(content)) {
			return Integer.parseInt(content);
		}
		return null;
	}

	/**
	 * http登录admin,获得cookie,由于地址可能有多个,最好是配置中的adminAddresses只写一个
	 */
	@Override
	public String login() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(
				RestTemplateUtil.builder().add("userName", xxlJobProperties.getUsername())
						.add("password", xxlJobProperties.getPassword()).build(),
				httpHeaders, HttpMethod.POST,
				URI.create(xxlJobProperties.getAdminAddresses().split(",")[0] + xxlJobProperties.getLoginPath()));
		ParameterizedTypeReference<ReturnT<String>> retType = new ParameterizedTypeReference<ReturnT<String>>() {
		};
		ResponseEntity<ReturnT<String>> responseEntity = restTemplate.exchange(requestEntity, retType);
		return getCookie(responseEntity);
	}

	@Override
	public Result<?> remove(Integer id) {
		get(Constants.XXLJOB_URL_API_REMOVE, MapTool.builder("id", id).build());
		return Result.ok();
	}

	// FIXME 未测试
	@Override
	public Result<?> removeByName(String groupName, String executorHandler) {
		get(Constants.XXLJOB_URL_API_REMOVE_BY_NAME,
				MapTool.builder("groupName", groupName).put("executorHandler", executorHandler).build());
		return Result.ok();
	}

	@Override
	public Result<?> start(Integer id) {
		get(Constants.XXLJOB_URL_API_START, MapTool.builder("id", id).build());
		return Result.ok();
	}

	@Override
	public Result<?> startByName(String groupName, String executorHandler) {
		get(Constants.XXLJOB_URL_API_START_BY_NAME,
				MapTool.builder("groupName", groupName).put("executorHandler", executorHandler).build());
		return Result.ok();
	}

	@Override
	public Result<?> stop(Integer id) {
		get(Constants.XXLJOB_URL_API_STOP, MapTool.builder("id", id).build());
		return Result.ok();
	}

	@Override
	public Result<?> stopByName(String groupName, String executorHandler) {
		get(Constants.XXLJOB_URL_API_STOP_BY_NAME,
				MapTool.builder("groupName", groupName).put("executorHandler", executorHandler).build());
		return Result.ok();
	}

	@Override
	public Result<?> trigger(Integer id, String executorParam, String addressList) {
		get(Constants.XXLJOB_URL_API_TRIGGER,
				MapTool.builder("id", id).put("executorParam", executorParam).put("addressList", addressList).build());
		return Result.ok();
	}

	@Override
	public Result<?> triggerByName(String groupName, String executorHandler, String executorParam, String addressList) {
		get(Constants.XXLJOB_URL_API_TRIGGER_BY_NAME,
				MapTool.builder("groupName", groupName).put("executorHandler", executorHandler)
						.put("executorParam", executorParam).put("addressList", addressList).build());
		return Result.ok();
	}

	@Override
	public Result<?> update(XxlJobInfo jobInfo) {

		// 测试数据
		jobInfo = XxlJobInfo.builder().jobGroup(1).id(11).jobDesc("test55").author("admin").scheduleType("CRON")
				.glueType("BEAN").executorRouteStrategy("FIRST").misfireStrategy("DO_NOTHING")
				.scheduleConf("0 0/1 * * * ?").executorHandler("demoJobHandler")
				.executorBlockStrategy("SERIAL_EXECUTION").build();

		// valid base
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
				throw new ResultException("Cron" + MessageService.getMessage("system_unvalid"));
			}
		} else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE /* || scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY */) {
			if (jobInfo.getScheduleConf() == null) {
				throw new ResultException(
						MessageService.getMessage("schedule_type") + MessageService.getMessage("system_unvalid"));
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

		// group valid
		Optional.ofNullable(jobInfo.getJobGroup()).orElseThrow(() -> new ResultException(
				MessageService.getMessage("jobinfo_field_jobgroup") + MessageService.getMessage("system_unvalid")));

		// stage job info
		Optional.ofNullable(jobInfo.getId()).orElseThrow(() -> new ResultException(
				MessageService.getMessage("jobinfo_field_id") + MessageService.getMessage("system_not_found")));

		postObject(Constants.XXLJOB_URL_API_UPDATE, jobInfo);
		return Result.ok();
	}
}