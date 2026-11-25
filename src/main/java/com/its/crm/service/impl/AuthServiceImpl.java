package com.its.crm.service.impl;

import com.its.crm.dto.auth.AuthResponse;
import com.its.crm.dto.auth.LoginRequest;
import com.its.crm.dto.auth.RegisterRequest;
import com.its.crm.entity.UserEntity;
import com.its.crm.exception.BadRequestException;
import com.its.crm.exception.ConflictException;
import com.its.crm.mapper.UserMapper;
import com.its.crm.repository.UserRepository;
import com.its.crm.security.JwtTokenProvider;
import com.its.crm.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered: " + request.getEmail());
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatar(request.getAvatar())
                .role(request.getRole())
                .build();

        UserEntity savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userMapper.toDTO(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userMapper.toDTO(user))
                .build();
    }
}
