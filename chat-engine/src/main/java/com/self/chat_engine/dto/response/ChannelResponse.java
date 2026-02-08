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

public class ChannelResponse {
    private UUID id;
    private String name;
    private String type; // TEXT, VOICE, ANNOUNCEMENT
    private UUID serverId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
