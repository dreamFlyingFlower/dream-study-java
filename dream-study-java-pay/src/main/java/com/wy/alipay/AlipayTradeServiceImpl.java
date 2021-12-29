package com.wy.alipay;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.wy.alipay.builder.AlipayTradePayRequestBuilder;
import com.wy.alipay.builder.AlipayTradeQueryRequestBuilder;
import com.wy.enums.TradeStatus;
import com.wy.lang.StrTool;
import com.wy.properties.AlipayProperties;
import com.wy.util.SpringContextUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-29 09:11:55
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class AlipayTradeServiceImpl extends AbsAlipayTradeService {

	public AlipayTradeServiceImpl(AlipayTradeServiceImpl.ClientBuilder builder) {
		if (StrTool.isEmpty(builder.getGatewayUrl())) {
			throw new NullPointerException("gatewayUrl should not be NULL!");
		} else if (StrTool.isEmpty(builder.getAppid())) {
			throw new NullPointerException("appid should not be NULL!");
		} else if (StrTool.isEmpty(builder.getPrivateKey())) {
			throw new NullPointerException("privateKey should not be NULL!");
		} else if (StrTool.isEmpty(builder.getFormat())) {
			throw new NullPointerException("format should not be NULL!");
		} else if (StrTool.isEmpty(builder.getCharset())) {
			throw new NullPointerException("charset should not be NULL!");
		} else if (StrTool.isEmpty(builder.getAlipayPublicKey())) {
			throw new NullPointerException("alipayPublicKey should not be NULL!");
		} else if (StrTool.isEmpty(builder.getSignType())) {
			throw new NullPointerException("signType should not be NULL!");
		} else {
			this.client = new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(),
					builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey(), builder.getSignType());
		}
	}

	public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder) {
		this.validateBuilder(builder);
		String outTradeNo = builder.getOutTradeNo();
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setNotifyUrl(builder.getNotifyUrl());
		String appAuthToken = builder.getAppAuthToken();
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.pay bizContent:" + request.getBizContent());
		AlipayTradePayResponse response = (AlipayTradePayResponse) this.getResponse(this.client, request);
		AlipayF2FPayResult result = new AlipayF2FPayResult(response);
		if (response != null && "10000".equals(response.getCode())) {
			result.setTradeStatus(TradeStatus.SUCCESS);
		} else {
			AlipayTradeQueryRequestBuilder queryBuiler;
			AlipayTradeQueryResponse queryResponse;
			if (response != null && "10003".equals(response.getCode())) {
				queryBuiler =
						(new AlipayTradeQueryRequestBuilder()).setAppAuthToken(appAuthToken).setOutTradeNo(outTradeNo);
				queryResponse = this.loopQueryResult(queryBuiler);
				return this.checkQueryAndCancel(outTradeNo, appAuthToken, result, queryResponse);
			}

			if (this.tradeError(response)) {
				queryBuiler =
						(new AlipayTradeQueryRequestBuilder()).setAppAuthToken(appAuthToken).setOutTradeNo(outTradeNo);
				queryResponse = this.tradeQuery(queryBuiler);
				return this.checkQueryAndCancel(outTradeNo, appAuthToken, result, queryResponse);
			}

			result.setTradeStatus(TradeStatus.FAILED);
		}

		return result;
	}

	@Getter
	@Setter
	public static class ClientBuilder {

		private String gatewayUrl;

		private String appid;

		private String privateKey;

		private String format;

		private String charset;

		private String alipayPublicKey;

		private String signType;

		public AlipayTradeServiceImpl build() {
			AlipayProperties properties = SpringContextUtils.getBean(AlipayProperties.class);
			if (StrTool.isEmpty(this.gatewayUrl)) {
				this.gatewayUrl = properties.getServerUrl();
			}
			if (StrTool.isEmpty(this.appid)) {
				this.appid = properties.getAppId();
			}
			if (StrTool.isEmpty(this.privateKey)) {
				this.privateKey = properties.getPrivateKey();
			}
			if (StrTool.isEmpty(this.format)) {
				this.format = "json";
			}
			if (StrTool.isEmpty(this.charset)) {
				this.charset = "utf-8";
			}
			if (StrTool.isEmpty(this.alipayPublicKey)) {
				this.alipayPublicKey = properties.getPublicKey();
			}
			if (StrTool.isEmpty(this.signType)) {
				this.signType = properties.getSignType();
			}
			return new AlipayTradeServiceImpl(this);
		}
	}
}