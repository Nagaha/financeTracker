package com.webapp.finance.dto;

import java.math.BigDecimal;

public record MonthlyAnalyticsResponse(
        int month,
        BigDecimal total
) {}
