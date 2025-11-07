package com.tastytown.backend.service;


import com.tastytown.backend.constants.Role;
import com.tastytown.backend.dto.AuthRequest;
import com.tastytown.backend.dto.AuthResponse;
import com.tastytown.backend.entity.User;

public interface IAuthService {
    
    User register(AuthRequest request);

    User register(AuthRequest request, Role role);

    AuthResponse login(AuthRequest request);
}
