package com.wy.processor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.enums.CrawlerEnum;
import com.wy.model.CrawlerConfigProperty;
import com.wy.processor.AbstractCrawlerPageProcessor;

import us.codecraft.webmagic.Page;

@Component
public class CrawlerInitPageProcessor extends AbstractCrawlerPageProcessor {

	@Autowired
	private CrawlerConfigProperty crawlerConfigProperty;

	@Override
	public void handelPage(Page page) {
		String initXpath = crawlerConfigProperty.getInitCrawlerXpath();
		List<String> helpUrl = page.getHtml().xpath(initXpath).links().all();
		addSpiderRequest(helpUrl, page.getRequest(), CrawlerEnum.DocumentType.HELP);
	}

	@Override
	public boolean isNeedHandelType(String handelType) {
		return CrawlerEnum.HandelType.FORWARD.name().equals(handelType);
	}

	@Override
	public boolean isNeedDocumentType(String documentType) {
		return CrawlerEnum.DocumentType.INIT.name().equals(documentType);
	}

	@Override
	public int getPriority() {
		return 100;
	}
}
