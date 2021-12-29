package com.wy.alipay.builder;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wy.lang.StrTool;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-29 09:15:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayTradeCancelRequestBuilder extends RequestBuilder {

	private AlipayTradeCancelRequestBuilder.BizContent bizContent = new AlipayTradeCancelRequestBuilder.BizContent();

	public AlipayTradeCancelRequestBuilder() {
	}

	public AlipayTradeCancelRequestBuilder.BizContent getBizContent() {
		return this.bizContent;
	}

	public boolean validate() {
		if (StrTool.isEmpty(this.bizContent.outTradeNo)) {
			throw new NullPointerException("out_trade_no should not be NULL!");
		} else {
			return true;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("AlipayTradeCancelRequestBuilder{");
		sb.append("bizContent=").append(this.bizContent);
		sb.append(", super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}

	public AlipayTradeCancelRequestBuilder setAppAuthToken(String appAuthToken) {
		return (AlipayTradeCancelRequestBuilder) super.setAppAuthToken(appAuthToken);
	}

	public AlipayTradeCancelRequestBuilder setNotifyUrl(String notifyUrl) {
		return (AlipayTradeCancelRequestBuilder) super.setNotifyUrl(notifyUrl);
	}

	public String getOutTradeNo() {
		return this.bizContent.outTradeNo;
	}

	public AlipayTradeCancelRequestBuilder setOutTradeNo(String outTradeNo) {
		this.bizContent.outTradeNo = outTradeNo;
		return this;
	}

	public static class BizContent {

		@JsonAlias("out_trade_no")
		private String outTradeNo;

		public BizContent() {
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("BizContent{");
			sb.append("outTradeNo='").append(this.outTradeNo).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}
}