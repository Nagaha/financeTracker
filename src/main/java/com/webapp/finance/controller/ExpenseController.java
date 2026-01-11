package com.webapp.finance.controller;

import com.webapp.finance.dto.*;
import com.webapp.finance.model.User;
import com.webapp.finance.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
