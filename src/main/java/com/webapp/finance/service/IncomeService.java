package com.webapp.finance.service;

import com.webapp.finance.dto.IncomeRequest;
import com.webapp.finance.dto.IncomeResponse;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.Income;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.IncomeRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeResponse create(IncomeRequest request, User user) {
        Income income = Income.builder()
                .user(user)
                .amount(request.getAmount())
                .source(request.getSource())
                .description(request.getDescription())
                .incomeDate(request.getIncomeDate())
                .build();

        Income saved = incomeRepository.save(income);
        return mapToResponse(saved);
    }

    public List<IncomeResponse> getAll(User user) {
        return incomeRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public IncomeResponse update(Long id, IncomeRequest request, User user) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ApiException("Income not found", HttpStatus.NOT_FOUND));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new ApiException("Forbidden", HttpStatus.FORBIDDEN);
        }

        income.setAmount(request.getAmount());
        income.setSource(request.getSource());
        income.setDescription(request.getDescription());
        income.setIncomeDate(request.getIncomeDate());

        return mapToResponse(incomeRepository.save(income));
    }

    public void delete(Long id, User user) {

    	Income income = incomeRepository.findById(id).
    						orElseThrow(() -> new ApiException("Income not found", HttpStatus.NOT_FOUND));
		
    	if(!income.getUser().getId().equals(user.getId()))
    	{
    		throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
    	}
    	
    	incomeRepository.delete(income);
	}
    
    private IncomeResponse mapToResponse(Income income) {
        return IncomeResponse.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .source(income.getSource())
                .description(income.getDescription())
                .incomeDate(income.getIncomeDate())
                .build();
    }

    public ByteArrayInputStream exportIncomesToExcel(List<IncomeResponse> incomeResponseList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        // Setting Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Source", "Amount", "Description", "Received Date"};

        // Date Cell style
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));


        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Adding data
        int rowIdx = 1;
        for (IncomeResponse income : incomeResponseList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(income.getSource());
            row.createCell(1).setCellValue(String.valueOf(income.getAmount()));
            row.createCell(2).setCellValue(income.getDescription());
            Cell dateCell = row.createCell(3);

            LocalDate localDate = income.getIncomeDate();
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
