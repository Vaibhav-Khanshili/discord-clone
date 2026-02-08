package com.self.chat_engine.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {

    @NotBlank(message = "Role is required")
    private String role; // ADMIN, MODERATOR, MEMBER

    private UUID userId;
    private UUID serverId;
}
