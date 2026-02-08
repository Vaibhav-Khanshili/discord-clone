package com.self.chat_engine.dto.response;

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
public class MemberResponse {

    private UUID id;
    private UUID userId;
    private String username;
    private String avatar;
    private String role; // OWNER, ADMIN, MODERATOR, MEMBER
    private LocalDateTime joinedAt;
}
