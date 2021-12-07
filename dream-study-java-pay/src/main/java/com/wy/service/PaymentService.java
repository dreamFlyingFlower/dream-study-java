package com.wy.service;

import java.util.Map;

import com.wy.model.TransferMoney;

/**
 * 第三方支付接口
 *
 * @author 飞花梦影
 * @date 2021-07-15 14:15:29
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface PaymentService {

	Object pay(String code, Map<String, Object> param);

	boolean getPayResult(String code, Map<String, Object> param);

	Object transfer(String code, TransferMoney transferMoney);
}