package com.webapp.finance.controller;

import com.webapp.finance.dto.DashboardResponse;
import com.webapp.finance.model.User;
import com.webapp.finance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse dashboard(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return dashboardService.dashboard(user, month, year);
    }
}
