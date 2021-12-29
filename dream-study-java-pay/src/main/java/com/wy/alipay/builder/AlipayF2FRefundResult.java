package com.wy.alipay.builder;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wy.alipay.AlipayResult;
import com.wy.enums.TradeStatus;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:19:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayF2FRefundResult implements AlipayResult {

	private TradeStatus tradeStatus;

	private AlipayTradeRefundResponse response;

	public AlipayF2FRefundResult(AlipayTradeRefundResponse response) {
		this.response = response;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public void setResponse(AlipayTradeRefundResponse response) {
		this.response = response;
	}

	public TradeStatus getTradeStatus() {
		return this.tradeStatus;
	}

	public AlipayTradeRefundResponse getResponse() {
		return this.response;
	}

	public boolean isTradeSuccess() {
		return this.response != null && TradeStatus.SUCCESS.equals(this.tradeStatus);
	}

}
