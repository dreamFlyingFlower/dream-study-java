package com.wy.util.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.wy.alipay.AlipayTradeService;
import com.wy.alipay.AlipayTradeServiceImpl;
import com.wy.common.Constants;
import com.wy.properties.AlipayProperties;
import com.wy.util.SpringContextUtils;

/**
 * 支付宝公共参数
 * 
 * @author 飞花梦影
 * @date 2021-12-29 10:32:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public final class AliPayConfig {

	private AliPayConfig() {
	}

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {

		private static AlipayProperties alipayProperties = SpringContextUtils.getBean(AlipayProperties.class);

		private static AlipayClient alipayClient = new DefaultAlipayClient(alipayProperties.getServerUrl(),
				alipayProperties.getAppId(), alipayProperties.getPrivateKey(), Constants.PARAM_TYPE, Constants.CHARSET,
				alipayProperties.getPublicKey(), Constants.SIGN_TYPE);

		private static AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
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
	public static AlipayTradeService getAlipayTradeService() {
		return SingletonHolder.tradeService;
	}
}