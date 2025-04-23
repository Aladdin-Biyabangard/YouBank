package com.aladdin.youbank001.utils;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.model.dtos.response.transactions.ResponseTransactionDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class Export {

    String path = "D:/Transaction History";

    public File exportToExcel( List<ResponseTransactionDto> transactionsDto, LocalDate start, LocalDate end) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transaction");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Transaction ID");
        header.createCell(1).setCellValue("Amount");
        header.createCell(2).setCellValue("Transaction Date");
        header.createCell(3).setCellValue("Description");
        header.createCell(4).setCellValue("Card");

        int rowNum = 1;
        for (ResponseTransactionDto dto : transactionsDto) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getTransactionId());
            row.createCell(1).setCellValue(dto.getAmount().toString());
            row.createCell(2).setCellValue(dto.getTransactionDate());
            row.createCell(3).setCellValue(dto.getDescription());
            row.createCell(4).setCellValue(dto.getCard());
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, "transaction_" + start + "_" + System.currentTimeMillis() + ".xlsx");

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            System.out.println("Excel faylı yaradıldı: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.getStackTrace();
            e.printStackTrace();
            e.getMessage();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
