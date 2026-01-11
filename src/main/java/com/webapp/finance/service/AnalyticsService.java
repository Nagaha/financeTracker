package com.webapp.finance.service;

import com.webapp.finance.dto.*;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ExpenseRepository expenseRepo;
    private final IncomeRepository incomeRepo;
    private final SavingRepository savingRepo;

    public List<MonthlyAnalyticsResponse> expenseMonthly(User user, int year) {
        return expenseRepo.monthlyExpense(user, year);
    }

    public List<MonthlyAnalyticsResponse> incomeMonthly(User user, int year) {
        return incomeRepo.monthlyIncome(user, year);
    }

    public List<MonthlyAnalyticsResponse> savingsMonthly(User user, int year) {
        return savingRepo.monthlySavings(user, year);
    }

    public List<ExpenseResponse> expenseReport(User user, int month , int year) {
        return expenseRepo.expenseReport(user, month, year);
    }

    public List<IncomeResponse> incomeReport(User user, int month , int year) {
        return incomeRepo.incomeReport(user, month, year);
    }

    public List<SavingResponse> savingsReport(User user, int month , int year) {
        return savingRepo.savingsReport(user, month, year);
    }
    
    public List<PieChartResponse> expenseByCategory(User user, int month, int year) {
        return expenseRepo.expenseByCategory(user, month, year)
                .stream()
                .map(o -> new PieChartResponse(
                        (String) o[0],
                        (BigDecimal) o[1]
                ))
                .collect(Collectors.toList());
    }
}
