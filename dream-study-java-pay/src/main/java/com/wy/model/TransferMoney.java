package com.wy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账
 *
 * @author 飞花梦影
 * @date 2021-07-15 14:17:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferMoney {

	/**
	 * 应用编号
	 */
	private Long appId;

	/**
	 * 创建人id
	 */
	private Long createdBy;

	/**
	 * 转账业务订单号
	 */
	private String outBizNo;

	/**
	 * 收款方识别方式
	 */
	private String payeeType;

	/**
	 * 收款方账号,可以是支付宝userId或者支付宝loginId
	 */
	private String payeeAccount;

	/**
	 * 转账金额,单位分
	 */
	private String amount;

	/**
	 * 付款方名称
	 */
	private String payerShowName;

	/**
	 * 收款方名称
	 */
	private String payeeRealName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 支付宝转账流水号
	 */
	private String orderId;
}