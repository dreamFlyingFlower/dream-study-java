package com.wy.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wy.collection.MapTool;
import com.wy.http.HttpTool;
import com.wy.properties.XxlJobProperties;
import com.wy.service.XxljobService;

/**
 * xxljob远程调用实现类
 *
 * @author 飞花梦影
 * @date 2022-01-04 17:32:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class XxljobServiceImpl implements XxljobService {

	@Autowired
	private XxlJobProperties xxlJobProperties;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * http登录admin,获得cookie,由于地址可能有多个,最好是配置中的adminAddresses只写一个
	 */
	@Override
	public String login() {
		String adminAddresses = xxlJobProperties.getAdminAddresses();
		String loginUrl = adminAddresses.split(",")[0] + xxlJobProperties.getLoginPath();
		try {
			String url = HttpTool.buildParams(loginUrl, MapTool.builder("userName", xxlJobProperties.getUsername())
					.put("password", xxlJobProperties.getPassword()).build());
			ResponseEntity<String> responseEntity =
					restTemplate.postForEntity(loginUrl, MapTool.builder("userName", xxlJobProperties.getUsername())
							.put("password", xxlJobProperties.getPassword()).build(), String.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				String body = responseEntity.getBody();
				System.out.println(body);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void pageList() {
		String adminAddresses = xxlJobProperties.getAdminAddresses();
		ResponseEntity<String> postForEntity =
				restTemplate.postForEntity(adminAddresses.split(",")[0] + "/jobinfo/pageList", null, String.class);
		System.out.println(postForEntity.getStatusCode());
		System.out.println(postForEntity.getBody());
	}
}