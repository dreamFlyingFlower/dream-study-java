package com.wy.alipay;

import com.wy.alipay.builder.AlipayF2FPrecreateResult;
import com.wy.alipay.builder.AlipayF2FQueryResult;
import com.wy.alipay.builder.AlipayF2FRefundResult;
import com.wy.alipay.builder.AlipayTradePayRequestBuilder;
import com.wy.alipay.builder.AlipayTradePrecreateRequestBuilder;
import com.wy.alipay.builder.AlipayTradeQueryRequestBuilder;
import com.wy.alipay.builder.AlipayTradeRefundRequestBuilder;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 17:58:55
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface AlipayTradeService {

	AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder var1);

	AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder var1);

	AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder var1);

	AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder var1);
}