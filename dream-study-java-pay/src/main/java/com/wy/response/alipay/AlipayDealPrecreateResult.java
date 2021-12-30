package com.wy.response.alipay;

import com.alipay.api.response.AlipayTradePrecreateResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:20:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public class AlipayDealPrecreateResult extends AbstractAlipayResult<AlipayTradePrecreateResponse> {

	public AlipayDealPrecreateResult(AlipayTradePrecreateResponse response) {
		this.response = response;
	}
}