package com.wy.easypoi;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-25 14:08:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	@Excel(name = "NO", needMerge = true, width = 20)
	private Integer no;

	@Excel(name = "name", needMerge = true, width = 20)
	private String name;

	@Excel(name = "shelfLife", width = 20, needMerge = true, exportFormat = "yyyy-MM-dd")
	private Date shelfLife;

	@ExcelCollection(name = "goodTypes")
	private List<GoodType> goodTypes;
}