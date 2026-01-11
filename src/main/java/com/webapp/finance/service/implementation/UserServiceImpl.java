package com.webapp.finance.service.implementation;

import com.webapp.finance.model.User;
import com.webapp.finance.repository.UserRepository;
import com.webapp.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
