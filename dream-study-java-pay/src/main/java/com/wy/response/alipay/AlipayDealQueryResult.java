package com.wy.response.alipay;

import com.alipay.api.response.AlipayTradeQueryResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:19:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public class AlipayDealQueryResult extends AbstractAlipayResult<AlipayTradeQueryResponse> {

	public AlipayDealQueryResult(AlipayTradeQueryResponse response) {
		this.response = response;
	}
}