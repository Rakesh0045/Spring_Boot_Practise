package com.tastytown.backend.helper;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

import com.tastytown.backend.entity.User;
import com.tastytown.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceHelper {
    
    private final UserRepository userRepository;

    public User getUserById(String userId){
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found with id: "+userId));
    }
}
