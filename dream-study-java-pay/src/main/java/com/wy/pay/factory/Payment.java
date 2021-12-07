package com.wy.pay.factory;

import java.util.Map;

import com.wy.model.TransferMoney;

/**
 * 支付接口
 *
 * @author 飞花梦影
 * @date 2021-12-07 15:52:14
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface Payment {

	/**
	 * 支付
	 * 
	 * @param t 支付参数
	 */
	Object pay(Map<String, Object> param);

	/**
	 * 支付成功的回调
	 * 
	 * @param param
	 */
	void paySuccess(Map<String, Object> param);

	/**
	 * 支付失败的回调
	 * 
	 * @param param
	 */
	void payFailure(Map<String, Object> param);

	/**
	 * 获得支付结果
	 */
	boolean getPayResult(Map<String, Object> param);

	/**
	 * 转账
	 * 
	 * @param transferMoney 参数
	 */
	Object transfer(TransferMoney transferMoney);
}