package com.wy.response.alipay;

import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:19:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayDealRefundResult extends AbstractAlipayResult<AlipayTradeRefundResponse> {

	public AlipayDealRefundResult(AlipayTradeRefundResponse response) {
		this.response = response;
	}
}