package com.self.chat_engine.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
    private String username;

    private String avatar;

    private String bio;

    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    private String currentPassword; // Required if changing password
}