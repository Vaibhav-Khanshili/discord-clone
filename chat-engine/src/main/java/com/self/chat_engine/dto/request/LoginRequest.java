package com.self.chat_engine.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class LoginRequest {

        @NotBlank(message = "Email or username is required")
        private String usernameOrEmail;

        @NotBlank(message = "Password is required")
        private String password;
}
