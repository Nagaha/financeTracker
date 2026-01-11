package com.webapp.finance.service;

import com.webapp.finance.dto.IncomeRequest;
import com.webapp.finance.dto.IncomeResponse;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.Income;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeResponse create(IncomeRequest request, User user) {
        Income income = Income.builder()
                .user(user)
                .amount(request.getAmount())
                .source(request.getSource())
                .description(request.getDescription())
                .incomeDate(request.getIncomeDate())
                .build();

        Income saved = incomeRepository.save(income);
        return mapToResponse(saved);
    }

    public List<IncomeResponse> getAll(User user) {
        return incomeRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public IncomeResponse update(Long id, IncomeRequest request, User user) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ApiException("Income not found", HttpStatus.NOT_FOUND));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new ApiException("Forbidden", HttpStatus.FORBIDDEN);
        }

        income.setAmount(request.getAmount());
        income.setSource(request.getSource());
        income.setDescription(request.getDescription());
        income.setIncomeDate(request.getIncomeDate());

        return mapToResponse(incomeRepository.save(income));
    }

    public void delete(Long id, User user) {

    	Income income = incomeRepository.findById(id).
    						orElseThrow(() -> new ApiException("Income not found", HttpStatus.NOT_FOUND));
		
    	if(!income.getUser().getId().equals(user.getId()))
    	{
    		throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
    	}
    	
    	incomeRepository.delete(income);
	}
    
    private IncomeResponse mapToResponse(Income income) {
        return IncomeResponse.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .source(income.getSource())
                .description(income.getDescription())
                .incomeDate(income.getIncomeDate())
                .build();
    }

}
