package com.webapp.finance.service;

import com.webapp.finance.dto.*;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.*;
import com.webapp.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseResponse create(ExpenseRequest request, User user) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ApiException("Category not found", HttpStatus.NOT_FOUND));

        Expense expense = Expense.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .description(request.getDescription())
                .expenseDate(request.getExpenseDate())
                .build();

        return toResponse(expenseRepository.save(expense));
    }

    public List<ExpenseResponse> getAll(User user) {
        return expenseRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ExpenseResponse update(Long id, ExpenseRequest request, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("Expense not found", HttpStatus.NOT_FOUND));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ApiException("Category not found", HttpStatus.NOT_FOUND));

        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());

        return toResponse(expenseRepository.save(expense));
    }

    public void delete(Long id, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("Expense not found", HttpStatus.NOT_FOUND));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponse toResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(),
                e.getAmount(),
                e.getDescription(),
                e.getExpenseDate(),
                e.getCategory().getName()
        );
    }
}
