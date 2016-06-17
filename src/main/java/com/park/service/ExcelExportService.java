package com.park.service;

import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import com.park.model.DataUsageCardDetail;
@Service
public class ExcelExportService {
	public void produceExceldataDataUsage(String title, String[] headers, List<DataUsageCardDetail> dataset,
			HSSFWorkbook workbook) {
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		for(int j=0;j<dataset.size();j++){
			HSSFRow row1 = sheet.createRow(j+1);
			DataUsageCardDetail dataUsageCardDetail=dataset.get(j);
		
//				HSSFCell cell0 = row1.createCell(0);				
//				cell0.setCellStyle(style2);
//				cell0.setCellValue(dataUsageCardDetail.getId());
			
				HSSFCell cell1 = row1.createCell(0);				
				cell1.setCellStyle(style2);
				cell1.setCellValue(dataUsageCardDetail.getParkName());
				
				HSSFCell cell2 = row1.createCell(1);				
				cell2.setCellStyle(style2);
				cell2.setCellValue(dataUsageCardDetail.getCardNumber());
			
				HSSFCell cell3 = row1.createCell(2);				
				cell3.setCellStyle(style2);
				cell3.setCellValue(dataUsageCardDetail.getPhoneNumber());
				
				HSSFCell cell7 = row1.createCell(3);				
				cell7.setCellStyle(style2);			
				cell7.setCellValue(dataUsageCardDetail.getDataUsage());
				
				HSSFCell cell4 = row1.createCell(4);				
				cell4.setCellStyle(style2);
				String typeValue="";
				switch (dataUsageCardDetail.getType()) {
				case 0:
					typeValue="出入口卡";
					break;
				case 1:
					typeValue="车位发布器卡";
					break;
				case 2:
					typeValue="室外车位卡";
					break;
				default:
					typeValue="未知类型";
					break;
				}
				cell4.setCellValue(typeValue);
				
				HSSFCell cell5 = row1.createCell(5);				
				cell5.setCellStyle(style2);			
				cell5.setCellValue(dataUsageCardDetail.getPosition());
				
				HSSFCell cell6 = row1.createCell(6);				
				cell6.setCellStyle(style2);
				String zuobiao="("+dataUsageCardDetail.getLatitude()+","+
				dataUsageCardDetail.getLongitude()+")";
				cell6.setCellValue(zuobiao);
				
				
				
			
		}
	}
}
