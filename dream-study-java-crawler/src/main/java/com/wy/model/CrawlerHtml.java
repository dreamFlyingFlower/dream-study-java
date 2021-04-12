package com.wy.model;

import java.util.List;

import com.wy.proxy.CrawlerProxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlerHtml {

	public CrawlerHtml(String url) {
		this.url = url;
	}

	private String url;

	private String html;

	private CrawlerProxy proxy;

	private List<CrawlerCookie> crawlerCookieList;
}