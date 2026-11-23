package com.its.crm.service;

import com.its.crm.dto.auth.AuthResponse;
import com.its.crm.dto.auth.LoginRequest;
import com.its.crm.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
