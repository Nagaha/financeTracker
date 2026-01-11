package com.webapp.finance.repository;

import com.webapp.finance.dto.MonthlyAnalyticsResponse;
import com.webapp.finance.dto.SavingResponse;
import com.webapp.finance.model.Saving;
import com.webapp.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SavingRepository extends JpaRepository<Saving, Long> {
	
    List<Saving> findByUser(User user);
    
    @Query("""
            SELECT new com.webapp.finance.dto.MonthlyAnalyticsResponse(
                EXTRACT(MONTH FROM s.savingDate),
                SUM(s.amount)
            )
            FROM Saving s
            WHERE s.user = :user
            AND EXTRACT(YEAR FROM s.savingDate) = :year
            GROUP BY EXTRACT(MONTH FROM s.savingDate)
            ORDER BY EXTRACT(MONTH FROM s.savingDate)
        """)
        List<MonthlyAnalyticsResponse> monthlySavings(User user, int year);

        @Query("""
            SELECT COALESCE(SUM(s.amount),0)
            FROM Saving s
            WHERE s.user = :user
            AND EXTRACT(MONTH FROM s.savingDate) = :month
            AND EXTRACT(YEAR FROM s.savingDate) = :year
        """)
        BigDecimal savingSummary(User user, int month, int year);
        
        @Query("""
    	    SELECT s.goal, SUM(s.amount)
    	    FROM Saving s
    	    WHERE s.user = :user
    	    AND EXTRACT(MONTH FROM s.savingDate) = :month
    	    AND EXTRACT(YEAR FROM s.savingDate) = :year
    	    GROUP BY s.goal
    	""")
    	List<Object[]> savingsByGoal(User user, int month, int year);
        	
    	@Query("""
    	    SELECT new com.webapp.finance.dto.SavingResponse(
                s.id,
                s.amount,
                s.goal,
                s.description,
                s.savingDate
            )
    	    FROM Saving s
    	    WHERE s.user = :user
    	    AND EXTRACT(MONTH FROM s.savingDate) = :month
    	    AND EXTRACT(YEAR FROM s.savingDate) = :year
    	    ORDER BY EXTRACT(DATE FROM s.savingDate)
    	""")
    	List<SavingResponse> savingsReport(User user, int month, int year);
}
