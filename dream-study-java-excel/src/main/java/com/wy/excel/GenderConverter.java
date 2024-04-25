package com.wy.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * EasyExcel枚举值转换
 *
 * @author 飞花梦影
 * @date 2024-04-01 13:50:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class GenderConverter implements Converter<Integer> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Integer convertToJavaData(ReadConverterContext<?> context) {
		return GenderType.convert(context.getReadCellData().getStringValue()).getValue();
	}

	@Override
	public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
		return new WriteCellData<>(GenderType.convert(context.getValue()).getDescription());
	}
}