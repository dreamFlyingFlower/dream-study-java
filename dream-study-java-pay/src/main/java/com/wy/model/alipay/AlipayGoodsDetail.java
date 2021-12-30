package com.wy.model.alipay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wy.lang.NumberTool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:07:27
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlipayGoodsDetail {

	@JsonAlias("goods_id")
	private String goodsId;

	@JsonAlias("alipay_goods_id")
	private String alipayGoodsId;

	@JsonAlias("goods_name")
	private String goodsName;

	private int quantity;

	private String price;

	@JsonAlias("goods_category")
	private String goodsCategory;

	private String body;

	public static AlipayGoodsDetail newInstance(String goodsId, String goodsName, long price, int quantity) {
		AlipayGoodsDetail info = new AlipayGoodsDetail();
		info.setGoodsId(goodsId);
		info.setGoodsName(goodsName);
		info.setPrice(price);
		info.setQuantity(quantity);
		return info;
	}

	public void setPrice(long price) {
		this.price = NumberTool.div(price, 100).toString();
	}
}