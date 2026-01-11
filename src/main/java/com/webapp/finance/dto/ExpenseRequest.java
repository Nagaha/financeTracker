package com.webapp.finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest {
    private Long categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
}
