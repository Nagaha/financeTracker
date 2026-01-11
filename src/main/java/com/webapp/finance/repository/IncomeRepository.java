package com.webapp.finance.repository;

import com.webapp.finance.dto.IncomeResponse;
import com.webapp.finance.dto.MonthlyAnalyticsResponse;
import com.webapp.finance.model.Income;
import com.webapp.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
	
    List<Income> findByUser(User user);
    
    	@Query("""
            SELECT new com.webapp.finance.dto.MonthlyAnalyticsResponse(
                EXTRACT(MONTH FROM i.incomeDate),
                SUM(i.amount)
            )
            FROM Income i
            WHERE i.user = :user
            AND EXTRACT(YEAR FROM i.incomeDate) = :year
            GROUP BY EXTRACT(MONTH FROM i.incomeDate)
            ORDER BY EXTRACT(MONTH FROM i.incomeDate)
        """)
        List<MonthlyAnalyticsResponse> monthlyIncome(User user, int year);

        @Query("""
            SELECT COALESCE(SUM(i.amount),0)
            FROM Income i
            WHERE i.user = :user
            AND EXTRACT(MONTH FROM i.incomeDate) = :month
            AND EXTRACT(YEAR FROM i.incomeDate) = :year
        """)
        BigDecimal incomeSummary(User user, int month, int year);
        
        @Query("""
            SELECT new com.webapp.finance.dto.IncomeResponse(
                i.id,
                i.amount,
                i.source,
                i.description,
                i.incomeDate
            )
            FROM Income i
            WHERE i.user = :user
            AND EXTRACT(YEAR FROM i.incomeDate) = :year
            AND EXTRACT(MONTH FROM i.incomeDate) =:month
            ORDER BY EXTRACT(DATE FROM i.incomeDate)
        """)
        List<IncomeResponse> incomeReport(User user, int month, int year);
}
