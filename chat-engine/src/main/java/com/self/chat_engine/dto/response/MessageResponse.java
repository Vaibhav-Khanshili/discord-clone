package com.self.chat_engine.dto.response;

import com.self.chat_engine.model.enums.MessageType;
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

public class MessageResponse {
    private UUID id;
    private String content;
    private UUID channelId;
    private UUID senderId;
    private String senderUsername;
    private String senderAvatar;
    private MessageType messageType;
    private String fileUrl;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean edited;
}
