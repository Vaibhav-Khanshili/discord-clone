package com.self.chat_engine.controller;

import com.self.chat_engine.dto.request.SendMessageRequest;
import com.self.chat_engine.dto.response.MessageResponse;
import com.self.chat_engine.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor

public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/channel/{channelId}")
    @SendTo("/topic/channel/{channelId}")
    public MessageResponse sendChannelMessage(
            @DestinationVariable UUID channelId,
            SendMessageRequest request) {
        return messageService.sendMessage(request);
    }

    @MessageMapping("/direct/{userId}")
    public void sendDirectMessage(
            @DestinationVariable UUID userId,
            SendMessageRequest request) {
        MessageResponse response = messageService.sendMessage(request);
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/messages",
                response
        );
    }

    @MessageMapping("/typing/channel/{channelId}")
    @SendTo("/topic/typing/{channelId}")
    public String broadcastTyping(@DestinationVariable UUID channelId, String username) {
        return username;
    }
}
