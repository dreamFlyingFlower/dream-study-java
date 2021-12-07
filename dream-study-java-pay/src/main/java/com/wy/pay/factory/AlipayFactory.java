package com.wy.pay.factory;

import com.wy.pay.Alipayment;

/**
 * 阿里支付工厂
 *
 * @author 飞花梦影
 * @date 2021-12-07 16:04:00
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AlipayFactory implements PaymentFactory {

	@Override
	public Payment getPayment() {
		return Alipayment.getInstance();
	}
}