package com.wy.alipay.builder;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wy.lang.StrTool;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:11:06
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayTradeQueryRequestBuilder extends RequestBuilder {

	private AlipayTradeQueryRequestBuilder.BizContent bizContent = new AlipayTradeQueryRequestBuilder.BizContent();

	public AlipayTradeQueryRequestBuilder() {
	}

	public AlipayTradeQueryRequestBuilder.BizContent getBizContent() {
		return this.bizContent;
	}

	public boolean validate() {
		if (StrTool.isEmpty(this.bizContent.tradeNo) && StrTool.isEmpty(this.bizContent.outTradeNo)) {
			throw new IllegalStateException("tradeNo and outTradeNo can not both be NULL!");
		} else {
			return true;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("AlipayTradeQueryRequestBuilder{");
		sb.append("bizContent=").append(this.bizContent);
		sb.append(", super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}

	public AlipayTradeQueryRequestBuilder setAppAuthToken(String appAuthToken) {
		return (AlipayTradeQueryRequestBuilder) super.setAppAuthToken(appAuthToken);
	}

	public AlipayTradeQueryRequestBuilder setNotifyUrl(String notifyUrl) {
		return (AlipayTradeQueryRequestBuilder) super.setNotifyUrl(notifyUrl);
	}

	public String getTradeNo() {
		return this.bizContent.tradeNo;
	}

	public AlipayTradeQueryRequestBuilder setTradeNo(String tradeNo) {
		this.bizContent.tradeNo = tradeNo;
		return this;
	}

	public String getOutTradeNo() {
		return this.bizContent.outTradeNo;
	}

	public AlipayTradeQueryRequestBuilder setOutTradeNo(String outTradeNo) {
		this.bizContent.outTradeNo = outTradeNo;
		return this;
	}

	public static class BizContent {

		@JsonAlias("trade_no")
		private String tradeNo;

		@JsonAlias("out_trade_no")
		private String outTradeNo;

		public BizContent() {
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("BizContent{");
			sb.append("tradeNo='").append(this.tradeNo).append('\'');
			sb.append(", outTradeNo='").append(this.outTradeNo).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}
}