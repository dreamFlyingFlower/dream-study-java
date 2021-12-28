package com.wy.alipay.builder;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wy.util.CommonUtils;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:07:27
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class GoodsDetail {

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

	public GoodsDetail() {
	}

	public static GoodsDetail newInstance(String goodsId, String goodsName, long price, int quantity) {
		GoodsDetail info = new GoodsDetail();
		info.setGoodsId(goodsId);
		info.setGoodsName(goodsName);
		info.setPrice(price);
		info.setQuantity(quantity);
		return info;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("GoodsDetail{");
		sb.append("goodsId='").append(this.goodsId).append('\'');
		sb.append(", alipayGoodsId='").append(this.alipayGoodsId).append('\'');
		sb.append(", goodsName='").append(this.goodsName).append('\'');
		sb.append(", quantity=").append(this.quantity);
		sb.append(", price='").append(this.price).append('\'');
		sb.append(", goodsCategory='").append(this.goodsCategory).append('\'');
		sb.append(", body='").append(this.body).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getGoodsId() {
		return this.goodsId;
	}

	public GoodsDetail setGoodsId(String goodsId) {
		this.goodsId = goodsId;
		return this;
	}

	public String getAlipayGoodsId() {
		return this.alipayGoodsId;
	}

	public GoodsDetail setAlipayGoodsId(String alipayGoodsId) {
		this.alipayGoodsId = alipayGoodsId;
		return this;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public GoodsDetail setGoodsName(String goodsName) {
		this.goodsName = goodsName;
		return this;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public GoodsDetail setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public String getPrice() {
		return this.price;
	}

	public GoodsDetail setPrice(long price) {
		this.price = CommonUtils.toAmount(price);
		return this;
	}

	public String getGoodsCategory() {
		return this.goodsCategory;
	}

	public GoodsDetail setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
		return this;
	}

	public String getBody() {
		return this.body;
	}

	public GoodsDetail setBody(String body) {
		this.body = body;
		return this;
	}
}