package com.webapp.finance.controller;

import com.webapp.finance.dto.SavingRequest;
import com.webapp.finance.dto.SavingResponse;
import com.webapp.finance.model.User;
import com.webapp.finance.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingController {

    private final SavingService savingService;

    @PostMapping
    public SavingResponse create(@RequestBody SavingRequest request,
                                 @AuthenticationPrincipal User user) {
        return savingService.create(request, user);
    }

    @GetMapping
    public List<SavingResponse> getAll(@AuthenticationPrincipal User user) {
        return savingService.getAll(user);
    }

    @PutMapping("/{id}")
    public SavingResponse update(@PathVariable Long id,
                                 @RequestBody SavingRequest request,
                                 @AuthenticationPrincipal User user) {
        return savingService.update(id, request, user);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
    					@AuthenticationPrincipal User user)
    {
    	savingService.delete(id, user);
    }
}
