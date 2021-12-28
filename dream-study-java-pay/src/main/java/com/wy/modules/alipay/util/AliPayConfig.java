package com.wy.modules.alipay.util;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.wy.properties.AlipayProperties;
import com.wy.util.SpringContextUtils;
/**
 * 配置公共参数
 */
public final class AliPayConfig {
	
    private AliPayConfig(){};
    /**
     * 签名方式
     */
 	public static String SIGN_TYPE = "RSA2";
	 /**
     * 参数类型
     */
    public static String PARAM_TYPE = "json";
    /**
     * 编码
     */
    public static String CHARSET = "utf-8";
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
    	
    	private static AlipayProperties alipayProperties = SpringContextUtils.getBean(AlipayProperties.class);
    	
		private  static AlipayClient alipayClient = new DefaultAlipayClient(
				alipayProperties.getServerUrl(), alipayProperties.getAppId(),
				alipayProperties.getPrivateKey(), PARAM_TYPE, CHARSET,
				alipayProperties.getPublicKey(),"RSA2");
		private  static AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }
    /**
     * 支付宝APP请求客户端实例
     * @return  AlipayClient
     */
    public static AlipayClient getAlipayClient(){
        return SingletonHolder.alipayClient;
    }
    /**
     * 电脑端预下单
     * @return  AlipayTradeService
     */
    public static AlipayTradeService getAlipayTradeService(){
        return SingletonHolder.tradeService;
    }
}