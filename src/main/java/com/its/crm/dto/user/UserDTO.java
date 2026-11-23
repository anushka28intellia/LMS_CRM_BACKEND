package com.its.crm.dto.user;

import com.its.crm.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private String avatar;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
