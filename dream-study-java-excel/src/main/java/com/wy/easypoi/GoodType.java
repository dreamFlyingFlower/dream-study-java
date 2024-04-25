package com.wy.easypoi;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-25 14:08:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class GoodType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Excel(name = "typeId", width = 20, height = 8)
	private String typeId;

	@Excel(name = "typeName", width = 20, height = 8)
	private String typeName;
}