package com.wy.request.alipay;

import java.util.regex.Pattern;

import com.wy.lang.StrTool;
import com.wy.model.alipay.AlipayDealRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:03:43
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class AlipayDealPayRequest extends AbstractAlipayDealRequest {

	private AlipayDealRequest alipayDealRequest;

	@Override
	public AlipayDealRequest getBizContent() {
		return this.alipayDealRequest;
	}

	@Override
	public boolean validate() {
		if (StrTool.isEmpty(this.alipayDealRequest.getScene())) {
			throw new NullPointerException("scene should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayDealRequest.getAuthCode())) {
			throw new NullPointerException("auth_code should not be NULL!");
		} else if (!Pattern.matches("^\\d{10,}$", this.alipayDealRequest.getAuthCode())) {
			throw new IllegalStateException("invalid auth_code!");
		} else if (StrTool.isEmpty(this.alipayDealRequest.getOutTradeNo())) {
			throw new NullPointerException("out_trade_no should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayDealRequest.getTotalAmount())) {
			throw new NullPointerException("total_amount should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayDealRequest.getSubject())) {
			throw new NullPointerException("subject should not be NULL!");
		} else if (StrTool.isEmpty(this.alipayDealRequest.getStoreId())) {
			throw new NullPointerException("store_id should not be NULL!");
		} else {
			return true;
		}
	}

	public AlipayDealPayRequest() {
		this.alipayDealRequest.setScene("bar_code");
	}
}