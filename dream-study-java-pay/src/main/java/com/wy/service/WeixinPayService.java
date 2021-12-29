package com.wy.service;

import com.wy.model.Product;

public interface WeixinPayService {

	/**
	 * 微信支付下单(模式二) 扫码支付 还有模式一 适合固定商品ID 有兴趣的同学可以自行研究
	 * 
	 * @param product
	 * @return String
	 */
	String weixinPay2(Product product);

	/**
	 * 微信支付下单(模式一)
	 * 
	 * @param product void
	 */
	void weixinPay1(Product product);

	/**
	 * 微信支付退款
	 * 
	 * @param product
	 * @return String
	 */
	String weixinRefund(Product product);

	/**
	 * 关闭订单
	 * 
	 * @param product
	 * @return String
	 */
	String weixinCloseorder(Product product);

	/**
	 * 下载微信账单
	 */
	void saveBill();

	/**
	 * 微信公众号支付返回一个url地址
	 * 
	 * @param product
	 * @return String
	 */
	String weixinPayMobile(Product product);

	/**
	 * H5支付 唤醒 微信APP 进行支付 申请入口：登录商户平台-->产品中心-->我的产品-->支付产品-->H5支付
	 * 
	 * @param product
	 * @return String
	 */
	String weixinPayH5(Product product);

	/**
	 * 查询订单
	 * 
	 * @param product
	 */
	void orderquery(Product product);
}