package com.wy.excel;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * 导出Excel的Order
 *
 * @author 飞花梦影
 * @date 2024-04-01 14:00:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class OrderExcelDTO {

	@ExcelProperty(value = "订单主键")
	@ColumnWidth(16)
	@EasyExcelMerge(merge = true, isPrimaryKey = true)
	private String id;

	@ExcelProperty(value = "订单编号")
	@ColumnWidth(20)
	@EasyExcelMerge(merge = true)
	private String orderId;

	@ExcelProperty(value = "收货地址")
	@EasyExcelMerge(merge = true)
	@ColumnWidth(20)
	private String address;

	@ExcelProperty(value = "创建时间")
	@ColumnWidth(20)
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	@EasyExcelMerge(merge = true)
	private Date createTime;

	@ExcelProperty(value = { "商品信息", "商品编号" })
	@ColumnWidth(20)
	private String productId;

	@ExcelProperty(value = { "商品信息", "商品名称" })
	@ColumnWidth(20)
	private String name;

	@ExcelProperty(value = { "商品信息", "商品标题" })
	@ColumnWidth(30)
	private String subtitle;

	@ExcelProperty(value = { "商品信息", "品牌名称" })
	@ColumnWidth(20)
	private String brandName;

	@ExcelProperty(value = { "商品信息", "商品价格" })
	@ColumnWidth(20)
	private BigDecimal price;

	@ExcelProperty(value = { "商品信息", "商品数量" })
	@ColumnWidth(20)
	private Integer count;
}