package com.wy.alipay.builder;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wy.lang.StrTool;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:17:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayTradeRefundRequestBuilder extends RequestBuilder {

	private AlipayTradeRefundRequestBuilder.BizContent bizContent = new AlipayTradeRefundRequestBuilder.BizContent();

	public AlipayTradeRefundRequestBuilder() {
	}

	public AlipayTradeRefundRequestBuilder.BizContent getBizContent() {
		return this.bizContent;
	}

	public boolean validate() {
		if (StrTool.isEmpty(this.bizContent.outTradeNo) && StrTool.isEmpty(this.bizContent.tradeNo)) {
			throw new NullPointerException("out_trade_no and trade_no should not both be NULL!");
		} else if (StrTool.isEmpty(this.bizContent.refundAmount)) {
			throw new NullPointerException("refund_amount should not be NULL!");
		} else if (StrTool.isEmpty(this.bizContent.refundReason)) {
			throw new NullPointerException("refund_reson should not be NULL!");
		} else {
			return true;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("AlipayTradeRefundRequestBuilder{");
		sb.append("bizContent=").append(this.bizContent);
		sb.append(", super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}

	public AlipayTradeRefundRequestBuilder setAppAuthToken(String appAuthToken) {
		return (AlipayTradeRefundRequestBuilder) super.setAppAuthToken(appAuthToken);
	}

	public AlipayTradeRefundRequestBuilder setNotifyUrl(String notifyUrl) {
		return (AlipayTradeRefundRequestBuilder) super.setNotifyUrl(notifyUrl);
	}

	public String getOutTradeNo() {
		return this.bizContent.outTradeNo;
	}

	public AlipayTradeRefundRequestBuilder setOutTradeNo(String outTradeNo) {
		this.bizContent.outTradeNo = outTradeNo;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setTradeNo(String tradeNo) {
		this.bizContent.tradeNo = tradeNo;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setRefundAmount(String refundAmount) {
		this.bizContent.refundAmount = refundAmount;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setOutRequestNo(String outRequestNo) {
		this.bizContent.outRequestNo = outRequestNo;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setRefundReason(String refundReason) {
		this.bizContent.refundReason = refundReason;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setStoreId(String storeId) {
		this.bizContent.storeId = storeId;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setAlipayStoreId(String alipayStoreId) {
		this.bizContent.alipayStoreId = alipayStoreId;
		return this;
	}

	public AlipayTradeRefundRequestBuilder setTerminalId(String terminalId) {
		this.bizContent.terminalId = terminalId;
		return this;
	}

	public String getTradeNo() {
		return this.bizContent.tradeNo;
	}

	public String getRefundAmount() {
		return this.bizContent.refundAmount;
	}

	public String getOutRequestNo() {
		return this.bizContent.outRequestNo;
	}

	public String getRefundReason() {
		return this.bizContent.refundReason;
	}

	public String getStoreId() {
		return this.bizContent.storeId;
	}

	public String getAlipayStoreId() {
		return this.bizContent.alipayStoreId;
	}

	public String getTerminalId() {
		return this.bizContent.terminalId;
	}

	public static class BizContent {

		@JsonAlias("trade_no")
		private String tradeNo;

		@JsonAlias("out_trade_no")
		private String outTradeNo;

		@JsonAlias("refund_amount")
		private String refundAmount;

		@JsonAlias("out_request_no")
		private String outRequestNo;

		@JsonAlias("refund_reason")
		private String refundReason;

		@JsonAlias("store_id")
		private String storeId;

		@JsonAlias("alipay_store_id")
		private String alipayStoreId;

		@JsonAlias("terminal_id")
		private String terminalId;

		public BizContent() {
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("BizContent{");
			sb.append("tradeNo='").append(this.tradeNo).append('\'');
			sb.append(", outTradeNo='").append(this.outTradeNo).append('\'');
			sb.append(", refundAmount='").append(this.refundAmount).append('\'');
			sb.append(", outRequestNo='").append(this.outRequestNo).append('\'');
			sb.append(", refundReason='").append(this.refundReason).append('\'');
			sb.append(", storeId='").append(this.storeId).append('\'');
			sb.append(", alipayStoreId='").append(this.alipayStoreId).append('\'');
			sb.append(", terminalId='").append(this.terminalId).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}
}