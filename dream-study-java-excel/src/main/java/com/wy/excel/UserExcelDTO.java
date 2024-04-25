package com.wy.excel;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * 导出Excel的User
 *
 * @author 飞花梦影
 * @date 2024-04-01 13:48:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class UserExcelDTO {

	@ExcelProperty("用户编号")
	@ColumnWidth(20)
	private Long id;

	@ExcelProperty("用户名")
	@ColumnWidth(20)
	private String username;

	@ExcelIgnore
	private String password;

	@ExcelProperty("昵称")
	@ColumnWidth(20)
	private String nickname;

	@ExcelProperty("生日")
	@ColumnWidth(20)
	@DateTimeFormat("yyyy-MM-dd")
	private Date birthday;

	@ExcelProperty("手机号")
	@ColumnWidth(20)
	private String phone;

	@ExcelProperty("身高（米）")
	@NumberFormat("#.##")
	@ColumnWidth(20)
	private Double height;

	@ExcelProperty(value = "性别", converter = GenderConverter.class)
	@ColumnWidth(10)
	private Integer gender;
}