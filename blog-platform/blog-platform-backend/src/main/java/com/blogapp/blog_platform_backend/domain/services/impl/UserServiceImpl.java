package com.blogapp.blog_platform_backend.domain.services.impl;

import com.blogapp.blog_platform_backend.domain.entities.Post;
import com.blogapp.blog_platform_backend.domain.entities.User;
import com.blogapp.blog_platform_backend.domain.repositories.UserRepository;
import com.blogapp.blog_platform_backend.domain.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+id));
    }


}
