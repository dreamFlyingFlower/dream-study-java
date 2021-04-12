package com.wy.callback;

import java.util.List;

import com.wy.proxy.CrawlerProxy;

/**
 * IP池更新回调
 * 
 * @author 飞花梦影
 * @date 2021-01-07 11:58:45
 * @git {@link https://github.com/mygodness100}
 */
public interface ProxyProviderCallBack {

	List<CrawlerProxy> getProxyList();
}