package com.wy.request.alipay;

import com.wy.lang.StrTool;
import com.wy.model.alipay.AlipayDealPrecreate;

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
 * @date 2021-12-28 18:16:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AlipayDealPrecreateRequest extends AbstractAlipayDealRequest {

	private AlipayDealPrecreate alipayContent;

	@Override
	public boolean validate() {
		if (StrTool.isEmpty(this.alipayContent.getOutTradeNo())) {
			throw new NullPointerException("out_trade_no should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayContent.getTotalAmount())) {
			throw new NullPointerException("total_amount should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayContent.getSubject())) {
			throw new NullPointerException("subject should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayContent.getStoreId())) {
			throw new NullPointerException("store_id should not be NULL!");
		} else {
			return true;
		}
	}

	@Override
	public Object getBizContent() {
		return this.alipayContent;
	}
}