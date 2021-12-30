package com.wy.model.alipay;

import com.fasterxml.jackson.annotation.JsonAlias;

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
 * @date 2021-12-30 10:01:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AlipayDealRefuse extends AlipayDealPrecreate {

	@JsonAlias("refund_amount")
	private String refundAmount;

	@JsonAlias("out_request_no")
	private String outRequestNo;

	@JsonAlias("refund_reason")
	private String refundReason;
}