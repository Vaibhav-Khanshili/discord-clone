package com.self.chat_engine.dto.request;

import com.self.chat_engine.model.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Channel ID is required")
    private UUID channelId;

    private UUID recipientId; // For direct messages

    @NotNull(message = "Message type is required")
    private MessageType messageType;

    private String fileUrl;

    private String fileName;
}
