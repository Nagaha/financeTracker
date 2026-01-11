package com.webapp.finance.service;

import com.webapp.finance.dto.*;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.*;
import com.webapp.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseResponse create(ExpenseRequest request, User user) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ApiException("Category not found", HttpStatus.NOT_FOUND));

        Expense expense = Expense.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .description(request.getDescription())
                .expenseDate(request.getExpenseDate())
                .build();

        return toResponse(expenseRepository.save(expense));
    }

    public List<ExpenseResponse> getAll(User user) {
        return expenseRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ExpenseResponse update(Long id, ExpenseRequest request, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("Expense not found", HttpStatus.NOT_FOUND));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ApiException("Category not found", HttpStatus.NOT_FOUND));

        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());

        return toResponse(expenseRepository.save(expense));
    }

    public void delete(Long id, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("Expense not found", HttpStatus.NOT_FOUND));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponse toResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(),
                e.getAmount(),
                e.getDescription(),
                e.getExpenseDate(),
                e.getCategory().getName()
        );
    }

    public ByteArrayInputStream exportExpensesToExcel(List<ExpenseResponse> expenseList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        // Setting Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Category", "Amount Spent", "Description", "Spent Date"};

        // Date cell type
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));


        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Adding data
        int rowIdx = 1;
        for (ExpenseResponse expense : expenseList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(expense.getCategoryName());
            row.createCell(1).setCellValue(String.valueOf(expense.getAmount()));
            row.createCell(2).setCellValue(expense.getDescription());
            Cell dateCell = row.createCell(3);

            LocalDate localDate = expense.getExpenseDate();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            dateCell.setCellValue(date);
            dateCell.setCellStyle(dateStyle);
        }

        // Auto size cols
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
