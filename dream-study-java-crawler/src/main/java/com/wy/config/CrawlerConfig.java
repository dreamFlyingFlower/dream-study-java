package com.wy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.callback.DataValidateCallBack;
import com.wy.collection.ListTool;
import com.wy.enums.CrawlerEnum;
import com.wy.lang.StrTool;
import com.wy.model.CrawlerConfigProperty;
import com.wy.model.ParseRule;
import com.wy.properties.ConfigProperties;
import com.wy.proxy.CrawlerProxyProvider;
import com.wy.utils.CookieHelper;
import com.wy.utils.CrawlerHelper;
import com.wy.utils.SeleniumClient;

import lombok.Getter;
import lombok.Setter;
import us.codecraft.webmagic.Spider;

@Configuration
@Getter
@Setter
public class CrawlerConfig {

	@Autowired
	private ConfigProperties config;

	/**
	 * 页面加载完成以后,js代码写入到cookie,用来验证区分人或机器访问,csdn网站反爬虫的一种方式
	 */
	@Value("${crux.cookie.name}")
	private String CRUX_COOKIE_NAME;

	public List<String> getInitCrawlerUrlList() {
		List<String> initCrawlerUrlList = new ArrayList<>();
		if (ListTool.isNotEmpty(config.getCrawler().getUrlSuffix())) {
			for (String string : config.getCrawler().getUrlSuffix()) {
				if (!string.toLowerCase().startsWith("http")) {
					initCrawlerUrlList.add(config.getCrawler().getUrlPrefix() + string);
				}
			}
		}
		return initCrawlerUrlList;
	}

	@Bean
	public SeleniumClient seleniumClient() {
		return new SeleniumClient();
	}

	/**
	 * 设置Cookie辅助类
	 *
	 * @return
	 */
	@Bean
	public CookieHelper cookieHelper() {
		return new CookieHelper(CRUX_COOKIE_NAME);
	}

	/**
	 * 数据校验匿名内部类
	 * 
	 * @param cookieHelper
	 * @return
	 */
	private DataValidateCallBack getDataValidateCallBack(CookieHelper cookieHelper) {
		return new DataValidateCallBack() {

			@Override
			public boolean validate(String content) {
				boolean flag = true;
				if (StrTool.isBlank(content)) {
					flag = false;
				} else {
					boolean isContains_acw_sc_v2 = content.contains("acw_sc__v2");
					boolean isContains_location_reload = content.contains("document.location.reload()");
					if (isContains_acw_sc_v2 && isContains_location_reload) {
						flag = false;
					}
				}
				return flag;
			}
		};
	}

	/**
	 * CrawerHelper 辅助类
	 *
	 * @return
	 */
	@Bean
	public CrawlerHelper getCrawerHelper() {
		CookieHelper cookieHelper = cookieHelper();
		CrawlerHelper crawerHelper = new CrawlerHelper();
		DataValidateCallBack dataValidateCallBack = getDataValidateCallBack(cookieHelper);
		crawerHelper.setDataValidateCallBack(dataValidateCallBack);
		return crawerHelper;
	}

	@Value("${proxy.isUsedProxyIp}")
	private Boolean isUsedProxyIp;

	/**
	 * CrawlerProxyProvider bean
	 *
	 * @return
	 */
	@Bean
	public CrawlerProxyProvider getCrawlerProxyProvider() {
		CrawlerProxyProvider crawlerProxyProvider = new CrawlerProxyProvider();
		crawlerProxyProvider.setUsedProxyIp(isUsedProxyIp);
		return crawlerProxyProvider;
	}

	/**
	 * 初始化抓取的Xpath
	 */
	private String initCrawlerXpath =
			"//ul[@class='feedlist_mod']/li[@class='clearfix']/div[@class='list_con']/dl[@class='list_userbar']/dd[@class='name']/a";

	/**
	 * 帮助页面抓取Xpath
	 */
	private String helpCrawlerXpath = "//div[@class='article-list']/div[@class='article-item-box']/h4/a";

	/**
	 * 是否开启帮助页面分页抓取
	 */
	@Value("${crawler.help.nextPagingSize}")
	private Integer crawlerHelpNextPagingSize;

	@Bean
	public CrawlerConfigProperty getCrawlerConfigProperty() {
		CrawlerConfigProperty crawlerConfigProperty = new CrawlerConfigProperty();
		// 初始化url列表
		crawlerConfigProperty.setInitCrawlerUrlList(getInitCrawlerUrlList());
		// 用户空间下的解析规则 url
		crawlerConfigProperty.setHelpCrawlerXpath(helpCrawlerXpath);
		// 目标页的解析规则
		crawlerConfigProperty.setTargetParseRuleList(getTargetParseRuleList());
		// 抓取用户空间下的页大小
		crawlerConfigProperty.setCrawlerHelpNextPagingSize(crawlerHelpNextPagingSize);
		// 初始化url解析规则定义
		crawlerConfigProperty.setInitCrawlerXpath(initCrawlerXpath);
		return crawlerConfigProperty;
	}

	/**
	 * 目标页的解析规则
	 * 
	 * @return
	 */
	private List<ParseRule> getTargetParseRuleList() {
		List<ParseRule> parseRules = new ArrayList<ParseRule>() {

			private static final long serialVersionUID = 1L;
			{
				// 标题
				add(new ParseRule("title", CrawlerEnum.ParseRuleType.XPATH, "//h1[@class='title-article']/text()"));
				// 作者
				add(new ParseRule("author", CrawlerEnum.ParseRuleType.XPATH, "//a[@class='follow-nickName']/text()"));
				// 发布日期
				add(new ParseRule("releaseDate", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='time']/text()"));
				// 标签
				add(new ParseRule("labels", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='tags-box']/a/text()"));
				// 个人空间
				add(new ParseRule("personalSpace", CrawlerEnum.ParseRuleType.XPATH,
						"//a[@class='follow-nickName']/@href"));
				// 阅读量
				add(new ParseRule("readCount", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='read-count']/text()"));
				// 点赞量
				add(new ParseRule("likes", CrawlerEnum.ParseRuleType.XPATH,
						"//div[@class='tool-box']/ul[@class='meau-list']/li[@class='btn-like-box']/button/p/text()"));
				// 回复次数
				add(new ParseRule("commentCount", CrawlerEnum.ParseRuleType.XPATH,
						"//div[@class='tool-box']/ul[@class='meau-list']/li[@class='to-commentBox']/a/p/text()"));
				// 文章内容
				add(new ParseRule("content", CrawlerEnum.ParseRuleType.XPATH, "//div[@id='content_views']/html()"));
			}
		};
		return parseRules;
	}

	private Spider spider;

	public Spider getSpider() {
		return spider;
	}

	public void setSpider(Spider spider) {
		this.spider = spider;
	}
}