package com.wy.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.wy.model.TransferMoney;
import com.wy.pay.factory.PaymentFactory;
import com.wy.pay.factory.Payments;
import com.wy.service.PaymentService;

/**
 * 第三方支付
 *
 * @author 飞花梦影
 * @date 2021-12-07 16:27:07
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 交易查询接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean getPayResult(String code, Map<String, Object> param) {
		PaymentFactory bean = applicationContext.getBean(Payments.getInstance(code));
		return bean.getPayment().getPayResult(param);
	}

	/**
	 * app支付
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public Object pay(String code, Map<String, Object> param) {
		PaymentFactory bean = applicationContext.getBean(Payments.getInstance(code));
		return bean.getPayment().pay(param).toString();
	}

	/**
	 * 转账接口
	 *
	 * @param transferParams
	 * @return
	 */
	@Override
	public Object transfer(String code, TransferMoney transferMoney) {
		PaymentFactory bean = applicationContext.getBean(Payments.getInstance(code));
		return bean.getPayment().transfer(transferMoney);
	}
}