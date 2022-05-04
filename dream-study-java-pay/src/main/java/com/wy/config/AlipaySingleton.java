package com.wy.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.wy.common.Constants;
import com.wy.properties.AlipayProperties;
import com.wy.service.AlipayDealService;
import com.wy.service.impl.AlipayDealServiceImpl;
import com.wy.util.SpringContextUtils;

/**
 * 支付宝公共参数
 * 
 * @author 飞花梦影
 * @date 2021-12-29 10:32:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public final class AlipaySingleton {

	private AlipaySingleton() {
	}

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {

		private static AlipayProperties alipayProperties = SpringContextUtils.getBean(AlipayProperties.class);

		private static AlipayClient alipayClient = new DefaultAlipayClient(alipayProperties.getServerUrl(),
				alipayProperties.getAppId(), alipayProperties.getPrivateKey(), Constants.PARAM_TYPE, Constants.CHARSET,
				alipayProperties.getPublicKey(), Constants.SIGN_TYPE);

		private static AlipayDealService alipayDealService = new AlipayDealServiceImpl.ClientBuilder().build();
	}

	/**
	 * 支付宝APP请求客户端实例
	 * 
	 * @return AlipayClient
	 */
	public static AlipayClient getAlipayClient() {
		return SingletonHolder.alipayClient;
	}

	/**
	 * 电脑端预下单
	 * 
	 * @return AlipayTradeService
	 */
	public static AlipayDealService getAlipayDealService() {
		return SingletonHolder.alipayDealService;
	}
}