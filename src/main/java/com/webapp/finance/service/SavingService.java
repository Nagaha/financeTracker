package com.webapp.finance.service;

import com.webapp.finance.dto.SavingRequest;
import com.webapp.finance.dto.SavingResponse;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.Saving;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.SavingRepository;
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
public class SavingService {

    private final SavingRepository savingRepository;

    public SavingResponse create(SavingRequest request, User user) {
        Saving saving = Saving.builder()
                .user(user)
                .amount(request.getAmount())
                .goal(request.getGoal())
                .description(request.getDescription())
                .savingDate(request.getSavingDate())
                .build();

        Saving saved = savingRepository.save(saving);
        return mapToResponse(saved);
    }

    public List<SavingResponse> getAll(User user) {
        return savingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SavingResponse update(Long id, SavingRequest request, User user) {
        Saving saving = savingRepository.findById(id)
                .orElseThrow(() -> new ApiException("Saving not found", HttpStatus.NOT_FOUND));

        if (!saving.getUser().getId().equals(user.getId())) {
            throw new ApiException("Forbidden", HttpStatus.FORBIDDEN);
        }

        saving.setAmount(request.getAmount());
        saving.setGoal(request.getGoal());
        saving.setDescription(request.getDescription());
        saving.setSavingDate(request.getSavingDate());

        return mapToResponse(savingRepository.save(saving));
    }

    public void delete(Long id, User user) {
    	
		Saving saving = savingRepository.findById(id).
							orElseThrow(() -> new ApiException("Saving not found", HttpStatus.NOT_FOUND));
		
		if(saving.getUser().getId().equals(user.getId()))
		{
			throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
		}
		
		savingRepository.delete(saving);
	}
    
    private SavingResponse mapToResponse(Saving saving) {
        return SavingResponse.builder()
                .id(saving.getId())
                .amount(saving.getAmount())
                .goal(saving.getGoal())
                .description(saving.getDescription())
                .savingDate(saving.getSavingDate())
                .build();
    }

    public List<String> getAllGoals(User user) {
        return savingRepository.findGoalsByUser(user);
    }

    public ByteArrayInputStream exportSavingsToExcel(List<SavingResponse> savingList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Savings");

        // Setting Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Goal", "Amount", "Description", "Saving Date"};

        // Date cell type
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));


        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Adding data
        int rowIdx = 1;
        for (SavingResponse saving : savingList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(saving.getGoal());
            row.createCell(1).setCellValue(String.valueOf(saving.getAmount()));
            row.createCell(2).setCellValue(saving.getDescription());
            Cell dateCell = row.createCell(3);

            LocalDate localDate = saving.getSavingDate();
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
