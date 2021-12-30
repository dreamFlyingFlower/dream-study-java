package com.wy.response.alipay;

import com.alipay.api.response.AlipayTradePayResponse;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 17:59:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayDealPayResult extends AbstractAlipayResult<AlipayTradePayResponse> {

	public AlipayDealPayResult(AlipayTradePayResponse response) {
		this.response = response;
	}
}