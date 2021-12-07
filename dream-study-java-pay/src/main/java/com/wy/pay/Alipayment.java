package com.wy.pay;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.wy.Constant;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.model.AlipayTransfer;
import com.wy.model.TransferMoney;
import com.wy.pay.factory.Payment;
import com.wy.properties.ConfigPropertes;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里支付
 *
 * @author 飞花梦影
 * @date 2021-12-07 16:01:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class Alipayment implements Payment, ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	private Alipayment() {
	}

	static class Inner {

		public static final Alipayment INSTANCE = new Alipayment();
	}

	public static Alipayment getInstance() {
		return Inner.INSTANCE;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Alipayment.applicationContext = applicationContext;
	}

	@Override
	public String pay(Map<String, Object> param) {
		// 这里和普通的接口调用不同,使用的是sdkExecute
		AlipayClient alipayClient = applicationContext.getBean(AlipayClient.class);
		ConfigPropertes config = applicationContext.getBean(ConfigPropertes.class);
		AlipayTradeAppPayModel tradeAppPayModel = new AlipayTradeAppPayModel();
		BeanUtils.copyProperties(param, tradeAppPayModel);
		AlipayTradeAppPayRequest t = new AlipayTradeAppPayRequest();
		tradeAppPayModel.setProductCode("QUICK_MSECURITY_PAY");
		t.setBizModel(tradeAppPayModel);
		t.setNotifyUrl(config.getAlipay().getNotifyUrl());
		try {
			return alipayClient.sdkExecute(t).getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void paySuccess(Map<String, Object> param) {
		// TODO Auto-generated method stub

	}

	@Override
	public void payFailure(Map<String, Object> param) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getPayResult(Map<String, Object> param) {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
		BeanUtils.copyProperties(param, queryModel);
		request.setBizModel(queryModel);
		AlipayClient alipayClient = applicationContext.getBean(AlipayClient.class);
		try {
			return alipayClient.certificateExecute(request).isSuccess();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Object transfer(TransferMoney transferMoney) {
		AlipayClient alipayClient = applicationContext.getBean(AlipayClient.class);
		String title = (StrTool.isNotBlank(transferMoney.getRemark()) ? transferMoney.getRemark() : "转账");
		// 转账请求入参
		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
		// 转账参数
		AlipayTransfer bizContent = new AlipayTransfer();
		bizContent.setOut_biz_no(transferMoney.getOutBizNo());
		bizContent.setTrans_amount(
				new BigDecimal(NumberTool.round(Math.abs(Integer.parseInt(transferMoney.getAmount())), 2)));
		bizContent.setProduct_code("TRANS_ACCOUNT_NO_PWD");
		bizContent.setBiz_scene("DIRECT_TRANSFER");
		bizContent.setOrder_title(title);
		Participant participant = new Participant();
		participant.setIdentity(transferMoney.getPayeeAccount());
		participant.setIdentityType(transferMoney.getPayeeType());
		participant.setName((StrTool.isNotBlank(transferMoney.getPayeeRealName()) ? transferMoney.getPayeeRealName()
				: Constant.Langes.STR_EMPTY));
		bizContent.setPayee_info(participant);
		bizContent.setRemark(title);
		request.setBizContent(JSON.toJSONString(bizContent));
		// 转账请求返回
		AlipayFundTransUniTransferResponse response = null;
		try {
			response = alipayClient.certificateExecute(request);
		} catch (Exception e) {
			log.info("doTransfer exception,异常信息:{}", e.toString());
			log.info("doTransfer exception,支付宝返回信息:{}", JSONObject.toJSONString(response));
		}
		log.info("doTransfer,AlipayFundTransUniTransferResponse:{}", JSONObject.toJSONString(response));
		return response;
	}
}