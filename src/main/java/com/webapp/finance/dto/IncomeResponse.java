package com.webapp.finance.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class IncomeResponse {
    private Long id;
    private BigDecimal amount;
    private String source;
    private String description;
    private LocalDate incomeDate;
}
