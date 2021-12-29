package com.wy.alipay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wy.alipay.builder.AlipayF2FPrecreateResult;
import com.wy.alipay.builder.AlipayF2FQueryResult;
import com.wy.alipay.builder.AlipayF2FRefundResult;
import com.wy.alipay.builder.AlipayTradeCancelRequestBuilder;
import com.wy.alipay.builder.AlipayTradePrecreateRequestBuilder;
import com.wy.alipay.builder.AlipayTradeQueryRequestBuilder;
import com.wy.alipay.builder.AlipayTradeRefundRequestBuilder;
import com.wy.enums.TradeStatus;
import com.wy.properties.AlipayProperties;
import com.wy.util.CommonUtils;
import com.wy.util.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-29 09:12:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public abstract class AbsAlipayTradeService extends AbsAlipayService implements AlipayTradeService {

	protected static ExecutorService executorService = Executors.newCachedThreadPool();

	protected AlipayClient client;

	AbsAlipayTradeService() {
	}

	public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder) {
		AlipayTradeQueryResponse response = this.tradeQuery(builder);
		AlipayF2FQueryResult result = new AlipayF2FQueryResult(response);
		if (this.querySuccess(response)) {
			result.setTradeStatus(TradeStatus.SUCCESS);
		} else if (this.tradeError(response)) {
			result.setTradeStatus(TradeStatus.UNKNOWN);
		} else {
			result.setTradeStatus(TradeStatus.FAILED);
		}

		return result;
	}

	protected AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryRequestBuilder builder) {
		this.validateBuilder(builder);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.query bizContent:" + request.getBizContent());
		return (AlipayTradeQueryResponse) this.getResponse(this.client, request);
	}

	public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder builder) {
		this.validateBuilder(builder);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setNotifyUrl(builder.getNotifyUrl());
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.refund bizContent:" + request.getBizContent());
		AlipayTradeRefundResponse response = (AlipayTradeRefundResponse) this.getResponse(this.client, request);
		AlipayF2FRefundResult result = new AlipayF2FRefundResult(response);
		if (response != null && "10000".equals(response.getCode())) {
			result.setTradeStatus(TradeStatus.SUCCESS);
		} else if (this.tradeError(response)) {
			result.setTradeStatus(TradeStatus.UNKNOWN);
		} else {
			result.setTradeStatus(TradeStatus.FAILED);
		}
		return result;
	}

	public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder) {
		this.validateBuilder(builder);
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setNotifyUrl(builder.getNotifyUrl());
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.precreate bizContent:" + request.getBizContent());
		AlipayTradePrecreateResponse response = (AlipayTradePrecreateResponse) this.getResponse(this.client, request);
		AlipayF2FPrecreateResult result = new AlipayF2FPrecreateResult(response);
		if (response != null && "10000".equals(response.getCode())) {
			result.setTradeStatus(TradeStatus.SUCCESS);
		} else if (this.tradeError(response)) {
			result.setTradeStatus(TradeStatus.UNKNOWN);
		} else {
			result.setTradeStatus(TradeStatus.FAILED);
		}

		return result;
	}

	protected AlipayF2FPayResult checkQueryAndCancel(String outTradeNo, String appAuthToken, AlipayF2FPayResult result,
			AlipayTradeQueryResponse queryResponse) {
		if (this.querySuccess(queryResponse)) {
			result.setTradeStatus(TradeStatus.SUCCESS);
			result.setResponse(this.toPayResponse(queryResponse));
			return result;
		} else {
			AlipayTradeCancelRequestBuilder builder = (new AlipayTradeCancelRequestBuilder()).setOutTradeNo(outTradeNo);
			builder.setAppAuthToken(appAuthToken);
			AlipayTradeCancelResponse cancelResponse = this.cancelPayResult(builder);
			if (this.tradeError(cancelResponse)) {
				result.setTradeStatus(TradeStatus.UNKNOWN);
			} else {
				result.setTradeStatus(TradeStatus.FAILED);
			}

			return result;
		}
	}

	protected AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelRequestBuilder builder) {
		this.validateBuilder(builder);
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
		request.setBizContent(builder.toJsonString());
		log.info("trade.cancel bizContent:" + request.getBizContent());
		return (AlipayTradeCancelResponse) this.getResponse(this.client, request);
	}

	protected AlipayTradeQueryResponse loopQueryResult(AlipayTradeQueryRequestBuilder builder) {
		AlipayTradeQueryResponse queryResult = null;
		AlipayProperties properties = SpringContextUtils.getBean(AlipayProperties.class);
		for (int i = 0; i < properties.getMaxQueryRetry(); ++i) {
			CommonUtils.sleep(properties.getQueryDuration());
			AlipayTradeQueryResponse response = this.tradeQuery(builder);
			if (response != null) {
				if (this.stopQuery(response)) {
					return response;
				}

				queryResult = response;
			}
		}

		return queryResult;
	}

	protected boolean stopQuery(AlipayTradeQueryResponse response) {
		return "10000".equals(response.getCode()) && ("TRADE_FINISHED".equals(response.getTradeStatus())
				|| "TRADE_SUCCESS".equals(response.getTradeStatus())
				|| "TRADE_CLOSED".equals(response.getTradeStatus()));
	}

	protected AlipayTradeCancelResponse cancelPayResult(AlipayTradeCancelRequestBuilder builder) {
		AlipayTradeCancelResponse response = this.tradeCancel(builder);
		if (this.cancelSuccess(response)) {
			return response;
		} else {
			if (this.needRetry(response)) {
				log.warn("begin async cancel request:" + builder);
				this.asyncCancel(builder);
			}

			return response;
		}
	}

	protected void asyncCancel(final AlipayTradeCancelRequestBuilder builder) {

		AlipayProperties properties = SpringContextUtils.getBean(AlipayProperties.class);
		executorService.submit(new Runnable() {

			public void run() {
				for (int i = 0; i < properties.getMaxCancelRetry(); ++i) {
					CommonUtils.sleep(properties.getCancelDuration());
					AlipayTradeCancelResponse response = AbsAlipayTradeService.this.tradeCancel(builder);
					if (AbsAlipayTradeService.this.cancelSuccess(response)
							|| !AbsAlipayTradeService.this.needRetry(response)) {
						return;
					}
				}

			}
		});
	}

	protected AlipayTradePayResponse toPayResponse(AlipayTradeQueryResponse response) {
		AlipayTradePayResponse payResponse = new AlipayTradePayResponse();
		payResponse.setCode(this.querySuccess(response) ? "10000" : "40004");
		StringBuilder msg =
				(new StringBuilder(response.getMsg())).append(" tradeStatus:").append(response.getTradeStatus());
		payResponse.setMsg(msg.toString());
		payResponse.setSubCode(response.getSubCode());
		payResponse.setSubMsg(response.getSubMsg());
		payResponse.setBody(response.getBody());
		payResponse.setParams(response.getParams());
		payResponse.setBuyerLogonId(response.getBuyerLogonId());
		payResponse.setFundBillList(response.getFundBillList());
		payResponse.setOpenId(response.getOpenId());
		payResponse.setOutTradeNo(response.getOutTradeNo());
		payResponse.setReceiptAmount(response.getReceiptAmount());
		payResponse.setTotalAmount(response.getTotalAmount());
		payResponse.setTradeNo(response.getTradeNo());
		return payResponse;
	}

	protected boolean needRetry(AlipayTradeCancelResponse response) {
		return response == null || "Y".equals(response.getRetryFlag());
	}

	protected boolean querySuccess(AlipayTradeQueryResponse response) {
		return response != null && "10000".equals(response.getCode())
				&& ("TRADE_SUCCESS".equals(response.getTradeStatus())
						|| "TRADE_FINISHED".equals(response.getTradeStatus()));
	}

	protected boolean cancelSuccess(AlipayTradeCancelResponse response) {
		return response != null && "10000".equals(response.getCode());
	}

	protected boolean tradeError(AlipayResponse response) {
		return response == null || "20000".equals(response.getCode());
	}
}
