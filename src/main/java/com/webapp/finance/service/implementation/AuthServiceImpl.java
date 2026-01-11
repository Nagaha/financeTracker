package com.webapp.finance.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.finance.dto.AuthResponse;
import com.webapp.finance.dto.LoginRequest;
import com.webapp.finance.dto.RegisterRequest;
import com.webapp.finance.exception.ApiException;
import com.webapp.finance.model.User;
import com.webapp.finance.repository.UserRepository;
import com.webapp.finance.security.JwtUtil;
import com.webapp.finance.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {

    	if (userRepository.existsByEmail(request.getEmail())) {
    	    throw new ApiException("Email already registered", HttpStatus.CONFLICT);
    	}

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Invalid credentials", HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid credentials", HttpStatus.BAD_REQUEST);
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
