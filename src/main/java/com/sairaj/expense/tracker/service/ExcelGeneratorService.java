package com.sairaj.expense.tracker.service;

import com.sairaj.expense.tracker.dto.ExpenseDetails;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelGeneratorService {
    public byte[] generateExcel(List<ExpenseDetails> expenseDetails) throws Exception{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");


        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Date");
        header.createCell(1).setCellValue("Amount");
        header.createCell(2).setCellValue("Category");
        header.createCell(3).setCellValue("Description");

        for(int i=0;i<4;i++){
            header.getCell(i).setCellStyle(style);
        }

        int rowNum = 1;

        for(ExpenseDetails expenseDetail:expenseDetails){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(expenseDetail.getExpenseDate());
            row.createCell(1).setCellValue(expenseDetail.getAmount());
            row.createCell(2).setCellValue(expenseDetail.getCategory());
            row.createCell(3).setCellValue(expenseDetail.getDescription());
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}
