package com.wy.model.alipay;

import java.util.List;

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
 * @date 2021-12-30 09:35:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AlipayDealPrecreate extends AlipayDealQuery {

	@JsonAlias("seller_id")
	private String sellerId;

	@JsonAlias("total_amount")
	private String totalAmount;

	@JsonAlias("discountable_amount")
	private String discountableAmount;

	@JsonAlias("undiscountable_amount")
	private String undiscountableAmount;

	private String subject;

	/**
	 * 附加信息,128长度
	 */
	private String body;

	@JsonAlias("goods_detail")
	private List<AlipayGoodsDetail> goodsDetails;

	@JsonAlias("operator_id")
	private String operatorId;

	@JsonAlias("store_id")
	private String storeId;

	@JsonAlias("alipay_store_id")
	private String alipayStoreId;

	@JsonAlias("terminal_id")
	private String terminalId;

	@JsonAlias("extend_params")
	private AlipayExtendParams extendParams;

	@JsonAlias("timeout_express")
	private String timeoutExpress;
}