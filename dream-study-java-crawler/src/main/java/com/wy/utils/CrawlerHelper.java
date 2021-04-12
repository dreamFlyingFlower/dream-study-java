package com.wy.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wy.callback.DataValidateCallBack;
import com.wy.enums.CrawlerEnum;
import com.wy.model.CrawlerHtml;
import com.wy.model.CrawlerParseItem;
import com.wy.model.ParseItem;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * 爬虫抓取辅助类,定义了爬虫下载过程中涉及到的各种下载内容校验以及对象的设置
 *
 * @author 飞花梦影
 * @date 2021-01-07 23:30:08
 * @git {@link https://github.com/mygodness100}
 */
@Slf4j
public class CrawlerHelper {

	/**
	 * 抓取保存请求数据的主键
	 */
	public final String CRAWLER_PROCESS_FLOW_DATA = "CRAWLER_PROCESS_FLOW_DATA";

	/**
	 * 数据转换主键
	 */
	private final String CRAWLER_PROCESS_PARSE_ITEM_DATA = "CRAWLER_PROCESS_PARSE_ITEM_DATA";

	/**
	 * 数据校验辅助类
	 */
	private DataValidateCallBack dataValidateCallBack;

	public DataValidateCallBack getDataValidateCallBack() {
		return dataValidateCallBack;
	}

	public void setDataValidateCallBack(DataValidateCallBack dataValidateCallBack) {
		this.dataValidateCallBack = dataValidateCallBack;
	}

	/**
	 * 获取ParseItem
	 *
	 * @param request
	 * @return
	 */
	public ParseItem getParseItem(Request request) {
		ParseItem parseItem = null;
		if (null != request) {
			Object parseItemObject = request.getExtra(CRAWLER_PROCESS_PARSE_ITEM_DATA);
			if (parseItemObject instanceof JSONObject) {
				parseItem = ((JSONObject) parseItemObject).toJavaObject(CrawlerParseItem.class);
			} else if (parseItemObject instanceof ParseItem) {
				parseItem = (ParseItem) parseItemObject;
			}
		}
		return parseItem;
	}

	/**
	 * 设置
	 *
	 * @return
	 */
	public void setParseItem(Request request, ParseItem parseItem) {
		if (null != request && null != parseItem) {
			Map<String, Object> extraMap = request.getExtras();
			if (null == extraMap) {
				extraMap = new HashMap<String, Object>();
				request.setExtras(extraMap);
			}
			if (!extraMap.containsKey(CRAWLER_PROCESS_PARSE_ITEM_DATA)) {
				extraMap.put(CRAWLER_PROCESS_PARSE_ITEM_DATA, parseItem);
			}
		}
	}

	/**
	 * 获取操作的处理类型
	 *
	 * @param request
	 * @return
	 */
	public String getHandelType(Request request) {
		String handelType = CrawlerEnum.HandelType.FORWARD.name();
		ParseItem parseItem = getParseItem(request);
		if (null != parseItem) {
			handelType = parseItem.getHandelType();
		}
		return handelType;
	}

	/**
	 * 获取操作的文档类型
	 *
	 * @param request
	 * @return
	 */
	public String getDocumentType(Request request) {
		String documentType = CrawlerEnum.DocumentType.OTHER.name();
		ParseItem parseItem = getParseItem(request);
		if (null != parseItem) {
			documentType = parseItem.getDocumentType();
		}
		return documentType;
	}

	/**
	 * 请求校验
	 *
	 * @param page
	 * @return
	 */
	public boolean requestValidation(Page page) {
		long currentTime = System.currentTimeMillis();
		log.info("开始校验下载数据,url:{}", page.getUrl());
		boolean flag = false;
		DataValidateCallBack dataValidateCallBack = getDataValidateCallBack();
		if (null != dataValidateCallBack) {
			flag = dataValidateCallBack.validate(page.getHtml().toString());
			log.info("校验数据状态,flag:{}", flag);
		}
		log.info("校验下载数据完成,url:{},状态:{},耗时:{}", page.getUrl(), flag, System.currentTimeMillis() - currentTime);
		return flag;
	}

	/**
	 * 请求校验
	 *
	 * @param crawlerHtml
	 * @return
	 */
	public boolean requestValidation(CrawlerHtml crawlerHtml) {
		boolean flag = false;
		long currentTime = System.currentTimeMillis();
		log.info("开始校验下载数据,url:{}", crawlerHtml.getUrl());
		if (null != crawlerHtml && StringUtils.isNotEmpty(crawlerHtml.getHtml())) {
			DataValidateCallBack dataValidateCallBack = getDataValidateCallBack();
			if (null != dataValidateCallBack) {
				flag = dataValidateCallBack.validate(crawlerHtml.getHtml().toString());
				log.info("校验数据状态,flag:{}", flag);
			}
		}
		log.info("下载数据完成,url:{},状态:{},耗时:{}", crawlerHtml.getUrl(), flag, System.currentTimeMillis() - currentTime);
		return flag;
	}
}