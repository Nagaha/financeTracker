package com.webapp.finance.controller;

import com.webapp.finance.dto.SavingRequest;
import com.webapp.finance.dto.SavingResponse;
import com.webapp.finance.model.User;
import com.webapp.finance.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    @GetMapping("/goals")
    public List<String> getAllGoals(@AuthenticationPrincipal User user) {
        return savingService.getAllGoals(user);
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportIncomes(@AuthenticationPrincipal User user) throws IOException {
        List<SavingResponse> savingsList = savingService.getAll(user);

        ByteArrayInputStream excelFile =
                savingService.exportSavingsToExcel(savingsList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=savings.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }
}
