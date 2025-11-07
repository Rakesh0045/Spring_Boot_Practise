package com.blogapp.blog_platform_backend.domain.services;

import com.blogapp.blog_platform_backend.domain.entities.Post;
import com.blogapp.blog_platform_backend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
