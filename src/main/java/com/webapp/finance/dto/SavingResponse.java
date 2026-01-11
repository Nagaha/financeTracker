package com.webapp.finance.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SavingResponse {
    private Long id;
    private BigDecimal amount;
    private String goal;
    private String description;
    private LocalDate savingDate;
}
