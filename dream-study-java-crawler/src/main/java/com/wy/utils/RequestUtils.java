package com.wy.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.wy.model.CrawlerCookie;

import us.codecraft.webmagic.Request;

public class RequestUtils {

	/**
	 * request 封装
	 *
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public static Request requestPackage(String url, Map<String, Object> headerMap) {
		Request request = null;
		if (StringUtils.isNotEmpty(url)) {
			request = new Request();
			request.setUrl(url);
			addHeader(request, headerMap);
		}
		return request;
	}

	/**
	 * 添加 cookie
	 *
	 * @param request
	 * @param headerMap
	 */
	public static void addHeader(Request request, Map<String, Object> headerMap) {
		if (null != headerMap && !headerMap.isEmpty()) {
			for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
				request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
	}

	/**
	 * 添加 cookie
	 *
	 * @param request
	 * @param cookieList
	 */
	public static void addCookie(Request request, List<CrawlerCookie> cookieList) {
		if (null != request && null != cookieList && !cookieList.isEmpty()) {
			for (CrawlerCookie cookie : cookieList) {
				if (null != cookie) {
					request.addCookie(cookie.getName(), cookie.getValue());
				}

			}
		}
	}

}
