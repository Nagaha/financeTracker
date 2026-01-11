package com.webapp.finance.dto;

import java.math.BigDecimal;

public record PieChartResponse(
        String label,
        BigDecimal amount
) {}
