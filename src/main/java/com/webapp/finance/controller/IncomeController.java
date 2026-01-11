package com.webapp.finance.controller;

import com.webapp.finance.dto.IncomeRequest;
import com.webapp.finance.dto.IncomeResponse;
import com.webapp.finance.model.User;
import com.webapp.finance.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public IncomeResponse create(@RequestBody IncomeRequest request,
                                 @AuthenticationPrincipal User user) {
        return incomeService.create(request, user);
    }

    @GetMapping
    public List<IncomeResponse> getAll(@AuthenticationPrincipal User user) {
        return incomeService.getAll(user);
    }

    @PutMapping("/{id}")
    public IncomeResponse update(@PathVariable Long id,
                                 @RequestBody IncomeRequest request,
                                 @AuthenticationPrincipal User user) {
        return incomeService.update(id, request, user);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
    					@AuthenticationPrincipal User user)
    {
    	incomeService.delete(id, user);
    }
}
