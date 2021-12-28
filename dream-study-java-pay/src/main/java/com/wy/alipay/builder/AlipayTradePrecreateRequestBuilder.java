package com.wy.alipay.builder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wy.lang.StrTool;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:16:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayTradePrecreateRequestBuilder extends RequestBuilder {

	private AlipayTradePrecreateRequestBuilder.BizContent bizContent =
			new AlipayTradePrecreateRequestBuilder.BizContent();

	public AlipayTradePrecreateRequestBuilder() {
	}

	public AlipayTradePrecreateRequestBuilder.BizContent getBizContent() {
		return this.bizContent;
	}

	public boolean validate() {
		if (StrTool.isEmpty(this.bizContent.outTradeNo)) {
			throw new NullPointerException("out_trade_no should not be NULL!");
		} else if (StrTool.isEmpty(this.bizContent.totalAmount)) {
			throw new NullPointerException("total_amount should not be NULL!");
		} else if (StrTool.isEmpty(this.bizContent.subject)) {
			throw new NullPointerException("subject should not be NULL!");
		} else if (StrTool.isEmpty(this.bizContent.storeId)) {
			throw new NullPointerException("store_id should not be NULL!");
		} else {
			return true;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("AlipayTradePrecreateRequestBuilder{");
		sb.append("bizContent=").append(this.bizContent);
		sb.append(", super=").append(super.toString());
		sb.append('}');
		return sb.toString();
	}

	public AlipayTradePrecreateRequestBuilder setAppAuthToken(String appAuthToken) {
		return (AlipayTradePrecreateRequestBuilder) super.setAppAuthToken(appAuthToken);
	}

	public AlipayTradePrecreateRequestBuilder setNotifyUrl(String notifyUrl) {
		return (AlipayTradePrecreateRequestBuilder) super.setNotifyUrl(notifyUrl);
	}

	public String getOutTradeNo() {
		return this.bizContent.outTradeNo;
	}

	public AlipayTradePrecreateRequestBuilder setOutTradeNo(String outTradeNo) {
		this.bizContent.outTradeNo = outTradeNo;
		return this;
	}

	public String getSellerId() {
		return this.bizContent.sellerId;
	}

	public AlipayTradePrecreateRequestBuilder setSellerId(String sellerId) {
		this.bizContent.sellerId = sellerId;
		return this;
	}

	public String getTotalAmount() {
		return this.bizContent.totalAmount;
	}

	public AlipayTradePrecreateRequestBuilder setTotalAmount(String totalAmount) {
		this.bizContent.totalAmount = totalAmount;
		return this;
	}

	public String getDiscountableAmount() {
		return this.bizContent.discountableAmount;
	}

	public AlipayTradePrecreateRequestBuilder setDiscountableAmount(String discountableAmount) {
		this.bizContent.discountableAmount = discountableAmount;
		return this;
	}

	public String getUndiscountableAmount() {
		return this.bizContent.undiscountableAmount;
	}

	public AlipayTradePrecreateRequestBuilder setUndiscountableAmount(String undiscountableAmount) {
		this.bizContent.undiscountableAmount = undiscountableAmount;
		return this;
	}

	public String getSubject() {
		return this.bizContent.subject;
	}

	public AlipayTradePrecreateRequestBuilder setSubject(String subject) {
		this.bizContent.subject = subject;
		return this;
	}

	public String getBody() {
		return this.bizContent.body;
	}

	public AlipayTradePrecreateRequestBuilder setBody(String body) {
		this.bizContent.body = body;
		return this;
	}

	public List<GoodsDetail> getGoodsDetailList() {
		return this.bizContent.goodsDetailList;
	}

	public AlipayTradePrecreateRequestBuilder setGoodsDetailList(List<GoodsDetail> goodsDetailList) {
		this.bizContent.goodsDetailList = goodsDetailList;
		return this;
	}

	public String getOperatorId() {
		return this.bizContent.operatorId;
	}

	public AlipayTradePrecreateRequestBuilder setOperatorId(String operatorId) {
		this.bizContent.operatorId = operatorId;
		return this;
	}

	public String getStoreId() {
		return this.bizContent.storeId;
	}

	public AlipayTradePrecreateRequestBuilder setStoreId(String storeId) {
		this.bizContent.storeId = storeId;
		return this;
	}

	public String getAlipayStoreId() {
		return this.bizContent.alipayStoreId;
	}

	public AlipayTradePrecreateRequestBuilder setAlipayStoreId(String alipayStoreId) {
		this.bizContent.alipayStoreId = alipayStoreId;
		return this;
	}

	public String getTerminalId() {
		return this.bizContent.terminalId;
	}

	public AlipayTradePrecreateRequestBuilder setTerminalId(String terminalId) {
		this.bizContent.terminalId = terminalId;
		return this;
	}

	public ExtendParams getExtendParams() {
		return this.bizContent.extendParams;
	}

	public AlipayTradePrecreateRequestBuilder setExtendParams(ExtendParams extendParams) {
		this.bizContent.extendParams = extendParams;
		return this;
	}

	public String getTimeoutExpress() {
		return this.bizContent.timeoutExpress;
	}

	public AlipayTradePrecreateRequestBuilder setTimeoutExpress(String timeoutExpress) {
		this.bizContent.timeoutExpress = timeoutExpress;
		return this;
	}

	public static class BizContent {

		@JsonAlias("out_trade_no")
		private String outTradeNo;

		@JsonAlias("seller_id")
		private String sellerId;

		@JsonAlias("total_amount")
		private String totalAmount;

		@JsonAlias("discountable_amount")
		private String discountableAmount;

		@JsonAlias("undiscountable_amount")
		private String undiscountableAmount;

		private String subject;

		private String body;

		@JsonAlias("goods_detail")
		private List<GoodsDetail> goodsDetailList;

		@JsonAlias("operator_id")
		private String operatorId;

		@JsonAlias("store_id")
		private String storeId;

		@JsonAlias("alipay_store_id")
		private String alipayStoreId;

		@JsonAlias("terminal_id")
		private String terminalId;

		@JsonAlias("extend_params")
		private ExtendParams extendParams;

		@JsonAlias("timeout_express")
		private String timeoutExpress;

		public BizContent() {
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("BizContent{");
			sb.append("outTradeNo='").append(this.outTradeNo).append('\'');
			sb.append(", sellerId='").append(this.sellerId).append('\'');
			sb.append(", totalAmount='").append(this.totalAmount).append('\'');
			sb.append(", discountableAmount='").append(this.discountableAmount).append('\'');
			sb.append(", undiscountableAmount='").append(this.undiscountableAmount).append('\'');
			sb.append(", subject='").append(this.subject).append('\'');
			sb.append(", body='").append(this.body).append('\'');
			sb.append(", goodsDetailList=").append(this.goodsDetailList);
			sb.append(", operatorId='").append(this.operatorId).append('\'');
			sb.append(", storeId='").append(this.storeId).append('\'');
			sb.append(", alipayStoreId='").append(this.alipayStoreId).append('\'');
			sb.append(", terminalId='").append(this.terminalId).append('\'');
			sb.append(", extendParams=").append(this.extendParams);
			sb.append(", timeoutExpress='").append(this.timeoutExpress).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}
}
