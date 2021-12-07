package com.wy.pay;

import java.util.Map;

import com.wy.model.TransferMoney;
import com.wy.pay.factory.Payment;

/**
 * 微信支付
 *
 * @author 飞花梦影
 * @date 2021-12-07 16:28:35
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WeixinPayment implements Payment {

	@Override
	public Object pay(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object transfer(TransferMoney transferMoney) {
		// TODO Auto-generated method stub
		return null;
	}
}