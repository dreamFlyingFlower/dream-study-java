package com.wy.alipay.builder;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.wy.alipay.AlipayResult;
import com.wy.enums.TradeStatus;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:20:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayF2FPrecreateResult implements AlipayResult {

	private TradeStatus tradeStatus;

	private AlipayTradePrecreateResponse response;

	public AlipayF2FPrecreateResult(AlipayTradePrecreateResponse response) {
		this.response = response;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public void setResponse(AlipayTradePrecreateResponse response) {
		this.response = response;
	}

	public TradeStatus getTradeStatus() {
		return this.tradeStatus;
	}

	public AlipayTradePrecreateResponse getResponse() {
		return this.response;
	}

	public boolean isTradeSuccess() {
		return this.response != null && TradeStatus.SUCCESS.equals(this.tradeStatus);
	}
}