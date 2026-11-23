package com.its.crm.service;

import com.its.crm.dto.user.UpdateUserRequest;
import com.its.crm.dto.user.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);

    UserDTO updateUser(UUID id, UpdateUserRequest request);

    void deleteUser(UUID id);
}
