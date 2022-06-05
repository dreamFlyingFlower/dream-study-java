package com.wy.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.wy.enums.CrawlerEnum;
import com.wy.model.CrawlerParseItem;
import com.wy.model.ParseItem;
import com.wy.model.ParseRule;
import com.wy.model.ProcessFlowData;
import com.wy.process.AbstractProcessFlow;
import com.wy.utils.CrawlerHelper;
import com.wy.utils.ParseRuleUtils;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
public abstract class AbstractCrawlerPageProcessor extends AbstractProcessFlow implements PageProcessor {

	public static void main(String[] args) {
		// 手动启动爬虫
		Spider.create(new AbstractCrawlerPageProcessor() {

			@Override
			public int getPriority() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public boolean isNeedHandelType(String handelType) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isNeedDocumentType(String documentType) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void handelPage(Page page) {
				// TODO Auto-generated method stub

			}
		})
				// 需要进行抓取的网页地址
				.addUrl("http://xxx.com").thread(2).runAsync();
	}

	@Override
	public void handler(ProcessFlowData processFlowData) {

	}

	@Override
	public CrawlerEnum.ComponentType getComponentType() {
		return CrawlerEnum.ComponentType.PAGEPROCESSOR;
	}

	@Resource
	private CrawlerPageProcessorManager crawlerPageProcessorManager;

	/**
	 * process是定制爬虫逻辑的核心接口方法,在这里编写抽取逻辑
	 * 
	 * @param page 页面对象
	 */
	@Override
	public void process(Page page) {
		long currentTimeMillis = System.currentTimeMillis();
		String handelType = crawlerHelper.getHandelType(page.getRequest());
		System.out.println("页面htm:" + page.getHtml());
		log.info("开始解析数据页面：url:{},handelType:{}", page.getUrl(), handelType);
		crawlerPageProcessorManager.handel(page);
		log.info("解析数据页面完成，url:{},handelType:{},耗时:{}", page.getUrl(), handelType,
				System.currentTimeMillis() - currentTimeMillis);
		// 解析多个url地址
		// page.addTargetRequest("http://需要解析的地址,也可以是页面中已经有的地址");
		// 同上,解析页面中已经存在的其他URL地址
		// page.addTargetRequests(page.getHtml().links().all());
		// 通过正则筛选页面的链接
		// page.addTargetRequests(page.getHtml().links().regex("http://xxx.com"));
		// 获得结果条目
		// page.getResultItems();
		// 跳过某些特定值
		// page.setSkip(true);
		// 将结果存储到page中
		// page.putField("test", "test");
		// 通过CSS获得html页面中的元素
		// page.getHtml().css("CSS选择器").links().all();
		// 获得指定元素的内容,如text,详见官网
		// page.getHtml().xpath("//p/span/text()").toString();
	}

	/**
	 * 爬虫配置
	 * 
	 * @return
	 */
	@Override
	public Site getSite() {
		Site site = Site.me().setRetryTimes(getRetryTimes()).setRetrySleepTime(getRetrySleepTime())
				.setSleepTime(getSleepTime()).setTimeOut(getTimeout());
		// header 配置
		Map<String, Object> headerMap = getHeaderMap();
		if (null != headerMap && !headerMap.isEmpty()) {
			for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
				site.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}

		return site;
	}

	/**
	 * 解析url列表
	 * 
	 * @param helpParseRuleList
	 * @return
	 */
	public List<String> getHelpUrlList(List<ParseRule> helpParseRuleList) {
		List<String> helpUrlList = new ArrayList<>();
		for (ParseRule parseRule : helpParseRuleList) {
			List<String> urlLinks = ParseRuleUtils.getUrlLinks(parseRule.getParseContentList());
			helpUrlList.addAll(urlLinks);
		}
		return helpUrlList;
	}

	@Autowired
	private CrawlerHelper crawlerHelper;

	/**
	 * 添加数据到爬虫列表
	 * 
	 * @param urlList
	 * @param request
	 * @param documentType
	 */
	public void addSpiderRequest(List<String> urlList, Request request, CrawlerEnum.DocumentType documentType) {
		List<ParseItem> parseItemList = new ArrayList<>();
		if (null != urlList && !urlList.isEmpty()) {
			for (String url : urlList) {
				CrawlerParseItem crawlerParseItem = new CrawlerParseItem();
				crawlerParseItem.setUrl(url);
				crawlerParseItem.setHandelType(crawlerHelper.getHandelType(request));
				crawlerParseItem.setDocumentType(documentType.name());
				parseItemList.add(crawlerParseItem);
			}
		}
		addSpiderRequest(parseItemList);
	}

	/**
	 * 处理页面
	 * 
	 * @param page
	 */
	public abstract void handelPage(Page page);

	/**
	 * 是否需要处理类型
	 * 
	 * @param handelType
	 * @return
	 */
	public abstract boolean isNeedHandelType(String handelType);

	/**
	 * 是否需要文档类型
	 * 
	 * @param documentType
	 * @return
	 */
	public abstract boolean isNeedDocumentType(String documentType);

	/**
	 * 重试次数
	 * 
	 * @return
	 */
	public int getRetryTimes() {
		return 3;
	}

	/**
	 * 重试间隔时间
	 * 
	 * @return
	 */
	public int getRetrySleepTime() {
		return 1000;
	}

	/**
	 * 抓取间隔时间
	 * 
	 * @return
	 */
	public int getSleepTime() {
		return 1000;
	}

	/**
	 * 超时时间
	 * 
	 * @return
	 */
	public int getTimeout() {
		return 10000;
	}
}