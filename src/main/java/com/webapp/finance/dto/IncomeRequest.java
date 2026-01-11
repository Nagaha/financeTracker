package com.webapp.finance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IncomeRequest {
    private BigDecimal amount;
    private String source;
    private String description;
    private LocalDate incomeDate;
}
