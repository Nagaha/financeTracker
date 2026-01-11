package com.webapp.finance.repository;

import com.webapp.finance.dto.ExpenseResponse;
import com.webapp.finance.dto.MonthlyAnalyticsResponse;
import com.webapp.finance.model.Expense;
import com.webapp.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
    List<Expense> findByUser(User user);
    
    @Query("""
            SELECT new com.webapp.finance.dto.MonthlyAnalyticsResponse(
                EXTRACT(MONTH FROM e.expenseDate),
                SUM(e.amount)
            )
            FROM Expense e
            WHERE e.user = :user
            AND EXTRACT(YEAR FROM e.expenseDate) = :year
            GROUP BY EXTRACT(MONTH FROM e.expenseDate)
            ORDER BY EXTRACT(MONTH FROM e.expenseDate)
        """)
        List<MonthlyAnalyticsResponse> monthlyExpense(User user, int year);

        @Query("""
            SELECT COALESCE(SUM(e.amount),0)
            FROM Expense e
            WHERE e.user = :user
            AND EXTRACT(MONTH FROM e.expenseDate) = :month
            AND EXTRACT(YEAR FROM e.expenseDate) = :year
        """)
        BigDecimal expenseSummary(User user, int month, int year);

        @Query("""
            SELECT c.name, SUM(e.amount)
            FROM Expense e
            JOIN e.category c
            WHERE e.user = :user
            AND EXTRACT(MONTH FROM e.expenseDate) = :month
            AND EXTRACT(YEAR FROM e.expenseDate) = :year
            GROUP BY c.name
        """)
        List<Object[]> expenseByCategory(User user, int month, int year);
        
        @Query("""
            SELECT new com.webapp.finance.dto.ExpenseResponse(
                e.id,
                e.amount,
                e.description,
                e.expenseDate,
                c.name
            )
            FROM Expense e
            JOIN e.category c
            WHERE e.user = :user
            AND EXTRACT(MONTH FROM e.expenseDate) = :month
            AND EXTRACT(YEAR FROM e.expenseDate) = :year
            ORDER BY EXTRACT(DATE FROM e.expenseDate)
        """)
        List<ExpenseResponse> expenseReport(User user, int month, int year);
}
