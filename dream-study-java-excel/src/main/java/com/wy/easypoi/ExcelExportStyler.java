package com.wy.easypoi;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;

/**
 * EasyPOI样式类
 *
 * @author 飞花梦影
 * @date 2024-04-25 14:02:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ExcelExportStyler implements IExcelExportStyler {

	public static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");

	public static final short FONT_SIZE_TEN = 10;

	public static final short FONT_SIZE_ELEVEN = 11;

	public static final short FONT_SIZE_TWELVE = 12;

	/**
	 * header style
	 */
	private CellStyle headerStyle;

	/**
	 * title style
	 */
	private CellStyle titleStyle;

	/**
	 * cell style
	 */
	private CellStyle styles;

	public ExcelExportStyler(Workbook workbook) {
		this.init(workbook);
	}

	/**
	 * init
	 *
	 * @param workbook
	 */
	private void init(Workbook workbook) {
		this.headerStyle = initHeaderStyle(workbook);
		this.titleStyle = initTitleStyle(workbook);
	}

	@Override
	public CellStyle getHeaderStyle(short color) {
		return headerStyle;
	}

	@Override
	public CellStyle getTitleStyle(short color) {
		return titleStyle;
	}

	@Override
	public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
		return styles;
	}

	@Override
	public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
		return getStyles(true, entity);
	}

	@Override
	public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
		return null;
	}

	/**
	 * init --HeaderStyle
	 *
	 * @param workbook
	 * @return
	 */
	private CellStyle initHeaderStyle(Workbook workbook) {
		CellStyle style = getBaseCellStyle(workbook);
		style.setFont(getFont(workbook, FONT_SIZE_TWELVE, true));
		return style;
	}

	/**
	 * init-TitleStyle
	 *
	 * @param workbook
	 * @return
	 */
	private CellStyle initTitleStyle(Workbook workbook) {
		CellStyle style = getBaseCellStyle(workbook);
		style.setFont(getFont(workbook, FONT_SIZE_ELEVEN, false));
		// ForegroundColor
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	/**
	 * BaseCellStyle
	 *
	 * @return
	 */
	private CellStyle getBaseCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		return style;
	}

	/**
	 * Font
	 *
	 * @param size
	 * @param isBold
	 * @return
	 */
	private Font getFont(Workbook workbook, short size, boolean isBold) {
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setBold(isBold);
		font.setFontHeightInPoints(size);
		return font;
	}
}