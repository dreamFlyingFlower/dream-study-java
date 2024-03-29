package com.wy.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import com.wy.callback.DelayedCallBack;
import com.wy.factory.CrawlerProxyFactory;
import com.wy.model.CrawlerCookie;
import com.wy.model.CrawlerHtml;
import com.wy.proxy.CrawlerProxy;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.selector.Html;

/**
 * Selenium工具类,使用selenium+webDriver+headless(无头浏览器模式),Chrome方式下载数据,回调模式以及延时模式
 * 
 * @author 飞花梦影
 * @date 2021-01-07 23:13:07
 * @git {@link https://github.com/mygodness100}
 */
@Slf4j
public class SeleniumClient {

	/**
	 * 默认超时时间
	 */
	private static final long TIMEOUT = 10000;

	/**
	 * 睡眠时间
	 */
	private static final long SLEEPTIME = 1000;

	/**
	 * 读取crawler.properties 配置文件
	 */
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("crawler");

	/**
	 * json数据的xpath 表达式
	 */
	public static final String SELENIUM_JSON_DATA_XPATH = "//pre/text()";

	/**
	 * 创建ChromeDriver驱动
	 *
	 * @param proxy 代理服务
	 * @return
	 */
	private SeleniumDriver initChromeDriver(Proxy proxy) {
		log.info("开始创建Chrome驱动");
		SeleniumDriver seleniumDriver = new SeleniumDriver();
		long currentTime = System.currentTimeMillis();
		// 创建service服务
		ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(resourceBundle.getString("webdriver.chrome.driver"))).usingAnyFreePort()
				.build();
		ChromeOptions chromeOptions = getChromeOptions(proxy);
		// 启动一个chrome 实例
		WebDriver webDriver = new ChromeDriver(chromeDriverService, chromeOptions);
		seleniumDriver.setChromeDriverService(chromeDriverService);
		seleniumDriver.setWebDriver(webDriver);
		log.info("创建Chrome驱动完成，耗时：" + (System.currentTimeMillis() - currentTime));
		return seleniumDriver;
	}

	/**
	 * 获取Chrome配置项
	 *
	 * @param proxy
	 * @return
	 */
	private ChromeOptions getChromeOptions(Proxy proxy) {
		// 获取chrome驱动的位置
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置无头模式
		chromeOptions.setHeadless(Boolean.TRUE);
		// 不使用沙箱运行
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		// 启动代理
		if (null != proxy) {
			chromeOptions.setProxy(proxy);
		}
		return chromeOptions;
	}

	/**
	 * 处理请求
	 *
	 * @param url 需要访问的URL
	 * @param proxy 处理的代理请求
	 * @param chromeCallback 执行完成的回调
	 */
	private void handler(String url, Proxy proxy, ChromeCallback chromeCallback) {
		SeleniumDriver seleniumDriver = null;
		if (StringUtils.isNotEmpty(url) && null != chromeCallback) {
			try {
				seleniumDriver = initChromeDriver(proxy);
				if (null != seleniumDriver && null != seleniumDriver.getWebDriver()) {
					chromeCallback.callBack(seleniumDriver.getWebDriver());
				}
			} catch (Exception e) {
				log.info("chrome调用失败：" + e.getMessage());
			} finally {
				log.info("关闭chrome驱动");
				closeChrome(seleniumDriver);
			}
		}
	}

	/**
	 * 获取Html
	 *
	 * @param url
	 * @return
	 */
	public CrawlerHtml getCrawlerHtml(String url, CrawlerProxy crawlerProxy, String cookieName) {
		log.info("Selenium开始抓取Html数据,url:{},cookieName:{},proxy:{}", url, cookieName, crawlerProxy);
		CrawlerHtml crawlerHtml = new CrawlerHtml(url);
		crawlerHtml.setProxy(crawlerProxy);
		Proxy proxy = null;
		if (null != crawlerProxy) {
			proxy = CrawlerProxyFactory.getSeleniumProxy(crawlerProxy);
		}

		handler(url, proxy, driver -> {
			driver.get(url);
			List<CrawlerCookie> crawlerCookieList = delayed(driver, cookieName);
			crawlerHtml.setCrawlerCookieList(crawlerCookieList);
			crawlerHtml.setHtml(driver.getPageSource());
		});
		log.info("Selenium 抓取Html数据结束,url:{},cookieName:{},cookieValue:{},proxy:{}", url, cookieName,
				crawlerHtml.getCrawlerCookieList(), crawlerProxy);
		return crawlerHtml;
	}

	/**
	 * 获取Cookie
	 *
	 * @param url
	 * @return
	 */
	public List<CrawlerCookie> getCookie(String url, CrawlerProxy proxy, String cookieName) {
		CrawlerHtml crawlerHtml = getCrawlerHtml(url, proxy, cookieName);
		return crawlerHtml.getCrawlerCookieList();
	}

	/**
	 * 获取Cookie的延时方法
	 * 
	 * 浏览器打开页面有可能js还未执行完成,获取的数据不准确,通过有没有写入一些特殊cookie判断页面是否已经加载完成<br>
	 * 如果没有这个cookie则页面一直重复循环,一直等到达到超时时间
	 *
	 * @param driver
	 * @param cookieName
	 */
	private List<CrawlerCookie> delayed(final WebDriver driver, final String cookieName) {
		List<CrawlerCookie> value = DelayedUtils.delayed(new DelayedCallBack<List<CrawlerCookie>>() {

			@Override
			public List<CrawlerCookie> callBack(long time) {
				Set<Cookie> cookieSet = driver.manage().getCookies();
				return getCrawlerCookie(cookieSet);
			}

			/**
			 * 判断是否存在该cookie,如果返回false,则一直循环不会退出直到达到超时时间
			 * 
			 * @return
			 */
			@Override
			public boolean isExist() {
				Set<Cookie> cookieSet = driver.manage().getCookies();
				return isExistCookieName(cookieSet, cookieName);
			}

			/**
			 * 每次循环获取需要睡眠的时间,防止一直获取cpu资源耗费的太多,默认1秒重复获取一次
			 * 
			 * @return
			 */
			@Override
			public long sleepTime() {
				return SLEEPTIME;
			}

			/**
			 * 配置的超时时间,防止页面没有找到cookie就一直在循环,没有退出;当达到超时时间就自动退出,将最新的页面数据返回
			 * 
			 * @return
			 */
			@Override
			public long timeOut() {
				return TIMEOUT;
			}
		});
		return value;
	}

	/**
	 * 判断是否包含需要的Cookie
	 *
	 * @param cookieSet
	 * @param cookieName
	 * @return
	 */
	private boolean isExistCookieName(Set<Cookie> cookieSet, String cookieName) {
		boolean flag = false;
		if (null != cookieSet && !cookieSet.isEmpty()) {
			for (Cookie cookie : cookieSet) {
				if (cookie.getName().equals(cookieName)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 获取 Cookie
	 *
	 * @param cookieSet
	 * @return
	 */
	private List<CrawlerCookie> getCrawlerCookie(Set<Cookie> cookieSet) {
		List<CrawlerCookie> crawlerCookieList = new ArrayList<CrawlerCookie>();
		for (Cookie cookie : cookieSet) {
			CrawlerCookie crawlerCookie = fillCrawlerCookie(cookie);
			if (null != crawlerCookie) {
				crawlerCookieList.add(crawlerCookie);
			}
		}
		return crawlerCookieList;
	}

	private CrawlerCookie fillCrawlerCookie(Cookie cookie) {
		CrawlerCookie crawlerCookie = new CrawlerCookie();
		crawlerCookie.setDomain(cookie.getDomain());
		crawlerCookie.setPath(cookie.getPath());
		crawlerCookie.setName(cookie.getName());
		crawlerCookie.setValue(cookie.getValue());
		crawlerCookie.setExpire(cookie.getExpiry());
		return crawlerCookie;
	}

	/**
	 * 获取json数据
	 *
	 * @param crawlerHtml
	 * @return
	 */
	public String getJsonData(CrawlerHtml crawlerHtml) {
		String jsonData = null;
		if (null != crawlerHtml) {
			String htmlStr = crawlerHtml.getHtml();
			if (StringUtils.isNotEmpty(htmlStr)) {
				jsonData = new Html(htmlStr).xpath(SELENIUM_JSON_DATA_XPATH).toString();
			}
		}
		return jsonData;
	}

	interface ChromeCallback {

		void callBack(WebDriver webDriver);
	}

	/**
	 * 关闭浏览器
	 */
	private void closeChrome(SeleniumDriver seleniumDriver) {
		if (null != seleniumDriver) {
			WebDriver webDriver = seleniumDriver.getWebDriver();
			ChromeDriverService chromeDriverService = seleniumDriver.getChromeDriverService();
			try {
				webDriver.quit();
			} finally {
				if (null != chromeDriverService) {
					chromeDriverService.stop();
				}
			}
			if (null != chromeDriverService) {
				if (chromeDriverService.isRunning()) {
					chromeDriverService.stop();
				}
			}
		}
	}

	@Getter
	@Setter
	class SeleniumDriver {

		private WebDriver webDriver;

		private ChromeDriverService chromeDriverService;
	}
}