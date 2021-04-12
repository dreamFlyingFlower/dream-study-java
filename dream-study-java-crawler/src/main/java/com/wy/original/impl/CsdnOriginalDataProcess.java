package com.wy.original.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.config.CrawlerConfig;
import com.wy.enums.CrawlerEnum;
import com.wy.model.CrawlerParseItem;
import com.wy.model.ParseItem;
import com.wy.model.ProcessFlowData;
import com.wy.original.AbstractOriginalDataProcess;

/**
 * 将url列表转换为对象
 * 
 * @author 飞花梦影
 * @date 2021-01-07 17:22:04
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class CsdnOriginalDataProcess extends AbstractOriginalDataProcess {

	@Autowired
	private CrawlerConfig crawlerConfig;

	@Override
	public List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData) {
		List<ParseItem> parseItemList = null;
		List<String> initCrawlerUrlList = crawlerConfig.getInitCrawlerUrlList();
		if (initCrawlerUrlList != null && !initCrawlerUrlList.isEmpty()) {
			parseItemList = initCrawlerUrlList.stream().map(url -> {
				CrawlerParseItem parseItem = new CrawlerParseItem();
				url = url + "?rnd=" + System.currentTimeMillis();
				parseItem.setUrl(url);
				parseItem.setDocumentType(CrawlerEnum.DocumentType.INIT.name());
				parseItem.setHandelType(processFlowData.getHandelType().name());
				return parseItem;
			}).collect(Collectors.toList());
		}
		return parseItemList;
	}

	@Override
	public int getPriority() {
		return 10;
	}
}