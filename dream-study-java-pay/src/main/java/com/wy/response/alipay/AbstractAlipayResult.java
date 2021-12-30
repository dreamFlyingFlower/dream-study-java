package com.wy.response.alipay;

import com.alipay.api.AlipayResponse;
import com.wy.enums.TradeStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 交易状态
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:00:09
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public abstract class AbstractAlipayResult<T extends AlipayResponse> {

	protected TradeStatus tradeStatus;

	protected T response;

	/**
	 * 交易成功与否
	 * 
	 * @return true->成功,false->失败
	 */
	public boolean tradeResult() {
		return this.response != null && TradeStatus.SUCCESS.equals(this.tradeStatus);
	}
}