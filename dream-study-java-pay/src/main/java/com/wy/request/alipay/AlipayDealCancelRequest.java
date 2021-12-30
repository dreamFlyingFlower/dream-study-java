package com.wy.request.alipay;

import com.wy.lang.StrTool;
import com.wy.model.alipay.AlipayDealQuery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-29 09:15:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AlipayDealCancelRequest extends AbstractAlipayDealRequest {

	private AlipayDealQuery alipayDealQuery;

	@Override
	public AlipayDealQuery getBizContent() {
		return this.alipayDealQuery;
	}

	@Override
	public boolean validate() {
		if (StrTool.isEmpty(this.alipayDealQuery.getOutTradeNo())) {
			throw new NullPointerException("out_trade_no should not be NULL!");
		} else {
			return true;
		}
	}
}