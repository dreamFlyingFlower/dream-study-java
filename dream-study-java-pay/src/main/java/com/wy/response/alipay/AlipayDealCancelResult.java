package com.wy.response.alipay;

import com.alipay.api.response.AlipayTradeCancelResponse;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 17:59:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayDealCancelResult extends AbstractAlipayResult<AlipayTradeCancelResponse> {

	public AlipayDealCancelResult(AlipayTradeCancelResponse response) {
		this.response = response;
	}
}