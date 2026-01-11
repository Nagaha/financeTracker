package com.webapp.finance.service;

import com.webapp.finance.dto.SavingRequest;
import com.webapp.finance.dto.SavingResponse;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.Saving;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.SavingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingService {

    private final SavingRepository savingRepository;

    public SavingResponse create(SavingRequest request, User user) {
        Saving saving = Saving.builder()
                .user(user)
                .amount(request.getAmount())
                .goal(request.getGoal())
                .description(request.getDescription())
                .savingDate(request.getSavingDate())
                .build();

        Saving saved = savingRepository.save(saving);
        return mapToResponse(saved);
    }

    public List<SavingResponse> getAll(User user) {
        return savingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SavingResponse update(Long id, SavingRequest request, User user) {
        Saving saving = savingRepository.findById(id)
                .orElseThrow(() -> new ApiException("Saving not found", HttpStatus.NOT_FOUND));

        if (!saving.getUser().getId().equals(user.getId())) {
            throw new ApiException("Forbidden", HttpStatus.FORBIDDEN);
        }

        saving.setAmount(request.getAmount());
        saving.setGoal(request.getGoal());
        saving.setDescription(request.getDescription());
        saving.setSavingDate(request.getSavingDate());

        return mapToResponse(savingRepository.save(saving));
    }

    public void delete(Long id, User user) {
    	
		Saving saving = savingRepository.findById(id).
							orElseThrow(() -> new ApiException("Saving not found", HttpStatus.NOT_FOUND));
		
		if(saving.getUser().getId().equals(user.getId()))
		{
			throw new ApiException("Unauthorized access", HttpStatus.FORBIDDEN);
		}
		
		savingRepository.delete(saving);
	}
    
    private SavingResponse mapToResponse(Saving saving) {
        return SavingResponse.builder()
                .id(saving.getId())
                .amount(saving.getAmount())
                .goal(saving.getGoal())
                .description(saving.getDescription())
                .savingDate(saving.getSavingDate())
                .build();
    }

    public List<String> getAllGoals(User user) {
        return savingRepository.findGoalsByUser(user);
    }
}
