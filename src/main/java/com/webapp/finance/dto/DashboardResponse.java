package com.webapp.finance.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal totalSavings,
        BigDecimal remainingAmount,
        boolean overspent,

        List<PieChartResponse> incomeDistribution,
        List<PieChartResponse> expenseByCategory,
        List<PieChartResponse> savingsByGoal
) {}
