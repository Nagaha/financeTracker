package com.webapp.finance.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.webapp.finance.dto.DashboardResponse;
import com.webapp.finance.dto.PieChartResponse;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.ExpenseRepository;
import com.webapp.finance.repository.IncomeRepository;
import com.webapp.finance.repository.SavingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeRepository incomeRepo;
    private final ExpenseRepository expenseRepo;
    private final SavingRepository savingRepo;

    public DashboardResponse dashboard(User user, int month, int year) {

        BigDecimal income = incomeRepo.incomeSummary(user, month, year);
        BigDecimal expense = expenseRepo.expenseSummary(user, month, year);
        BigDecimal savings = savingRepo.savingSummary(user, month, year);

        BigDecimal remaining = income.subtract(expense).subtract(savings);
        boolean overspent = remaining.compareTo(BigDecimal.ZERO) < 0;

        /* -------- PIE 1: INCOME DISTRIBUTION -------- */
        List<PieChartResponse> incomePie = List.of(
                new PieChartResponse("Expense", expense),
                new PieChartResponse("Savings", savings),
                new PieChartResponse(
                        "Remaining",
                        remaining.compareTo(BigDecimal.ZERO) > 0
                                ? remaining
                                : BigDecimal.ZERO
                )
        );

        /* -------- PIE 2: EXPENSE BY CATEGORY -------- */
        List<PieChartResponse> expenseCategoryPie =
                expenseRepo.expenseByCategory(user, month, year)
                        .stream()
                        .map(o -> new PieChartResponse(
                                (String) o[0],
                                (BigDecimal) o[1]
                        ))
                        .toList();

        /* -------- PIE 3: SAVINGS BY GOAL -------- */
        List<PieChartResponse> savingsGoalPie =
                savingRepo.savingsByGoal(user, month, year)
                        .stream()
                        .map(o -> new PieChartResponse(
                                (String) o[0],
                                (BigDecimal) o[1]
                        ))
                        .toList();

        return new DashboardResponse(
                income,
                expense,
                savings,
                remaining,
                overspent,
                incomePie,
                expenseCategoryPie,
                savingsGoalPie
        );
    }
}
