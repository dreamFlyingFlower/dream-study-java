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
 * @date 2021-12-28 18:11:06
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AlipayDealQueryRequest extends AbstractAlipayDealRequest {

	private AlipayDealQuery alipayTradeQuery;

	@Override
	public AlipayDealQuery getBizContent() {
		return this.alipayTradeQuery;
	}

	@Override
	public boolean validate() {
		if (StrTool.isEmpty(this.alipayTradeQuery.getTradeNo())
				&& StrTool.isEmpty(this.alipayTradeQuery.getOutTradeNo())) {
			throw new IllegalStateException("tradeNo and outTradeNo can not both be NULL!");
		} else {
			return true;
		}
	}
}