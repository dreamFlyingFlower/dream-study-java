package com.wy.service;

import com.wy.request.alipay.AlipayDealPayRequest;
import com.wy.request.alipay.AlipayDealPrecreateRequest;
import com.wy.request.alipay.AlipayDealQueryRequest;
import com.wy.request.alipay.AlipayDealRefundRequest;
import com.wy.response.alipay.AlipayDealPayResult;
import com.wy.response.alipay.AlipayDealPrecreateResult;
import com.wy.response.alipay.AlipayDealQueryResult;
import com.wy.response.alipay.AlipayDealRefundResult;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 17:58:55
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface AlipayDealService {

	AlipayDealPayResult tradePay(AlipayDealPayRequest var1);

	AlipayDealQueryResult queryTradeResult(AlipayDealQueryRequest var1);

	AlipayDealRefundResult tradeRefund(AlipayDealRefundRequest var1);

	AlipayDealPrecreateResult tradePrecreate(AlipayDealPrecreateRequest var1);
}