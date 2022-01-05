package com.wy.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wy.collection.MapTool;
import com.wy.config.MessageService;
import com.wy.enums.ExecutorRouteStrategyEnum;
import com.wy.enums.MisfireStrategyEnum;
import com.wy.enums.ScheduleTypeEnum;
import com.wy.http.HttpClientTools;
import com.wy.lang.AssertTool;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.model.XxlJobInfo;
import com.wy.properties.XxlJobProperties;
import com.wy.result.ResultException;
import com.wy.service.XxlJobService;
import com.wy.util.CronExpression;
import com.xxl.job.core.biz.model.ReturnT;
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
public class XxlJobServiceImpl implements XxlJobService {

	private static final String XXLJOB_COOKIE_LOGIN_KEY = "XXL_JOB_LOGIN_IDENTITY";

	private static final Map<String, String> USER_COOKIE = new HashMap<>();

	@Autowired
	private XxlJobProperties xxlJobProperties;

	/**
	 * 从登录的请求接口中获得cookie
	 * 
	 * @param responseEntity 登录响应结果
	 * @return cookie值
	 */
	private String getCookie(HttpResponse httpResponse) {
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				// 拿不到结果
				System.out.println(EntityUtils.toString(httpResponse.getEntity()));
				ReturnT<String> ret =
						JSON.parseObject(AssertTool.notBlank(EntityUtils.toString(httpResponse.getEntity())).toString(),
								new TypeReference<ReturnT<String>>() {
								});
				if (200 == ret.getCode()) {
					// Set-Cookie=[XXL_JOB_LOGIN_IDENTITY=cookie; Path=/; HttpOnly]
					String cookie = httpResponse.getHeaders(HttpHeaders.SET_COOKIE)[0].getValue();
					int start = cookie.indexOf(XXLJOB_COOKIE_LOGIN_KEY) + 1 + XXLJOB_COOKIE_LOGIN_KEY.length();
					cookie = cookie.substring(start, cookie.indexOf(";", start));
					USER_COOKIE.put(xxlJobProperties.getUsername(), cookie);
					return cookie;
				}
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

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
	 * 构建带cookie的请求头
	 * 
	 * @return 请求头
	 */
	private Map<String, Object> getHttpHeaders() {
		String cookie = getCookie();
		return MapTool.builder("Content-Type", "application/x-www-form-urlencoded")
				.put(HttpHeaders.COOKIE, new ArrayList<>(Arrays.asList(XXLJOB_COOKIE_LOGIN_KEY + "=" + cookie)))
				.build();
	}

	/**
	 * 获得结果集
	 * 
	 * @param <T>
	 * @param api 接口地址
	 * @param httpMethod 请求方式
	 * @param t 参数
	 * @return 结果
	 */
	private <T> String getResponse(String api, HttpMethod httpMethod, T t) {
		// 返回指定泛型的结果
		HttpResponse httpResponse = null;
		if (Objects.isNull(t)) {
		} else {
			httpResponse = HttpClientTools.sendPostForm(xxlJobProperties.getAdminAddresses().split(",")[0] + api,
					JSON.parseObject(JSON.toJSONString(t), new TypeReference<Map<String, String>>() {
					}), StandardCharsets.UTF_8, getHttpHeaders());
		}
		if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
			// ReturnT<String> ret = responseEntity.getBody();
			// if (200 == ret.getCode()) {
			// return ret.getContent();
			// } else {
			// throw new ResultException(ret.getMsg());
			// }
		}
		return null;
	}

	/**
	 * http登录admin,获得cookie,由于地址可能有多个,最好是配置中的adminAddresses只写一个
	 */
	@Override
	public String login() {
		HashMap<String, String> hashMap = new HashMap<>(4);
		hashMap.put("userName", "admin");
		hashMap.put("password", "123456");
		HttpResponse httpResponse = HttpClientTools.sendPostForm(
				xxlJobProperties.getAdminAddresses().split(",")[0] + xxlJobProperties.getLoginPath(), hashMap,
				StandardCharsets.UTF_8,
				MapTool.builder(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded").build());
		// Header[] allHeaders = httpResponse.getAllHeaders();
		// for (Header header : allHeaders) {
		// System.out.println(header.getName() + ":" + header.getValue());
		// }
		return getCookie(httpResponse);
	}

	/**
	 * 新怎定时任务
	 * 
	 * @param jobInfo 定时任务信息
	 */
	@Override
	public Integer add(XxlJobInfo jobInfo1) {

		XxlJobInfo jobInfo =
				XxlJobInfo.builder().jobGroup(1).jobDesc("test").author("admin").scheduleType("CRON").glueType("BEAN")
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

		String content = getResponse("/jobinfo/add", HttpMethod.POST, jobInfo);
		if (StrTool.isNotBlank(content) && NumberTool.isNumber(content)) {
			return Integer.parseInt(content);
		}
		return null;
	}
}