package com.wy.request.alipay;

import com.wy.lang.StrTool;
import com.wy.model.alipay.AlipayDealRefuse;

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
 * @date 2021-12-28 18:17:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AlipayDealRefundRequest extends AbstractAlipayDealRequest {

	private AlipayDealRefuse alipayTradeRefuse;

	@Override
	public AlipayDealRefuse getBizContent() {
		return this.alipayTradeRefuse;
	}

	@Override
	public boolean validate() {
		if (StrTool.isEmpty(this.alipayTradeRefuse.getOutTradeNo())
				&& StrTool.isEmpty(this.alipayTradeRefuse.getTradeNo())) {
			throw new NullPointerException("out_trade_no and trade_no should not both be NULL!");
		} else if (StrTool.isEmpty(this.alipayTradeRefuse.getRefundAmount())) {
			throw new NullPointerException("refund_amount should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayTradeRefuse.getRefundReason())) {
			throw new NullPointerException("refund_reson should not be NULL!");
		} else {
			return true;
		}
	}
}