package com.wy.alipay;

import com.alipay.api.response.AlipayTradePayResponse;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 17:59:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayF2FPayResult implements AlipayResult {

	private TradeStatus tradeStatus;

	private AlipayTradePayResponse response;

	public AlipayF2FPayResult(AlipayTradePayResponse response) {
		this.response = response;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public void setResponse(AlipayTradePayResponse response) {
		this.response = response;
	}

	public TradeStatus getTradeStatus() {
		return this.tradeStatus;
	}

	public AlipayTradePayResponse getResponse() {
		return this.response;
	}

	public boolean isTradeSuccess() {
		return this.response != null && TradeStatus.SUCCESS.equals(this.tradeStatus);
	}
}