package com.wy.alipay.builder;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.wy.alipay.AlipayResult;
import com.wy.alipay.TradeStatus;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:19:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayF2FQueryResult implements AlipayResult {

	private TradeStatus tradeStatus;

	private AlipayTradeQueryResponse response;

	public AlipayF2FQueryResult(AlipayTradeQueryResponse response) {
		this.response = response;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public void setResponse(AlipayTradeQueryResponse response) {
		this.response = response;
	}

	public TradeStatus getTradeStatus() {
		return this.tradeStatus;
	}

	public AlipayTradeQueryResponse getResponse() {
		return this.response;
	}

	public boolean isTradeSuccess() {
		return this.response != null && TradeStatus.SUCCESS.equals(this.tradeStatus);
	}
}
