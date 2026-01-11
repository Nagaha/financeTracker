package com.webapp.finance.controller;

import com.webapp.finance.dto.*;
import com.webapp.finance.model.User;
import com.webapp.finance.service.ExpenseService;
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
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse create(
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User user) {
        return expenseService.create(request, user);
    }

    @GetMapping
    public List<ExpenseResponse> list(
            @AuthenticationPrincipal User user) {
        return expenseService.getAll(user);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User user) {
        return expenseService.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        expenseService.delete(id, user);
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportExpenses(@AuthenticationPrincipal User user) throws IOException {
        List<ExpenseResponse> expenseList = expenseService.getAll(user);

        ByteArrayInputStream excelFile =
                expenseService.exportExpensesToExcel(expenseList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expenses.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }
}
