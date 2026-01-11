package com.webapp.finance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SavingRequest {
    private BigDecimal amount;
    private String goal;
    private String description;
    private LocalDate savingDate;
}
