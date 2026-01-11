package com.webapp.finance.service;

import com.webapp.finance.dto.AuthResponse;
import com.webapp.finance.dto.LoginRequest;
import com.webapp.finance.dto.RegisterRequest;

public interface AuthService {
	
    void register(RegisterRequest request);
    
    AuthResponse login(LoginRequest request);
}
