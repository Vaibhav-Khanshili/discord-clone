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
public class InviteResponse {

    private String code;
    private UUID serverId;
    private String serverName;
    private String serverIcon;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
