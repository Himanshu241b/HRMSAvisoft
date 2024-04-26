package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElse(null);
    }
}
