package com.webapp.finance.controller;

import com.webapp.finance.dto.*;
import com.webapp.finance.model.User;
import com.webapp.finance.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/expenses/monthly")
    public List<MonthlyAnalyticsResponse> expenseMonthly(
            @AuthenticationPrincipal User user,
            @RequestParam int year) {
        return analyticsService.expenseMonthly(user, year);
    }

    @GetMapping("/income/monthly")
    public List<MonthlyAnalyticsResponse> incomeMonthly(
            @AuthenticationPrincipal User user,
            @RequestParam int year) {
        return analyticsService.incomeMonthly(user, year);
    }

    @GetMapping("/savings/monthly")
    public List<MonthlyAnalyticsResponse> savingsMonthly(
            @AuthenticationPrincipal User user,
            @RequestParam int year) {
        return analyticsService.savingsMonthly(user, year);
    }

    @GetMapping("/expenses")
    public List<ExpenseResponse> expenseMonthReport(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return analyticsService.expenseReport(user, month, year);
    }

    @GetMapping("/income")
    public List<IncomeResponse> incomeMonthReport(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return analyticsService.incomeReport(user, month, year);
    }

    @GetMapping("/savings")
    public List<SavingResponse> savingsMonthReport(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return analyticsService.savingsReport(user, month, year);
    }
    
    @GetMapping("/expenses/category")
    public List<PieChartResponse> expenseByCategory(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return analyticsService.expenseByCategory(user, month, year);
    }
}
