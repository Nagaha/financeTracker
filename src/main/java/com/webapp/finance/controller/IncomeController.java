package com.webapp.finance.controller;

import com.webapp.finance.dto.IncomeRequest;
import com.webapp.finance.dto.IncomeResponse;
import com.webapp.finance.model.User;
import com.webapp.finance.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public IncomeResponse create(@RequestBody IncomeRequest request,
                                 @AuthenticationPrincipal User user) {
        return incomeService.create(request, user);
    }

    @GetMapping
    public List<IncomeResponse> getAll(@AuthenticationPrincipal User user) {
        return incomeService.getAll(user);
    }

    @PutMapping("/{id}")
    public IncomeResponse update(@PathVariable Long id,
                                 @RequestBody IncomeRequest request,
                                 @AuthenticationPrincipal User user) {
        return incomeService.update(id, request, user);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
    					@AuthenticationPrincipal User user)
    {
    	incomeService.delete(id, user);
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportIncomes(@AuthenticationPrincipal User user) throws IOException {
        List<IncomeResponse> incomeList = incomeService.getAll(user);

        ByteArrayInputStream excelFile =
                incomeService.exportIncomesToExcel(incomeList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=incomes.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }
}
