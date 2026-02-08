package com.self.chat_engine.controller;

import com.self.chat_engine.dto.request.SendMessageRequest;
import com.self.chat_engine.dto.response.MessageResponse;
import com.self.chat_engine.services.DirectMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/direct-messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class DirectMessageController {
    private final DirectMessageService directMessageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendDirectMessage(@Valid @RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(directMessageService.sendDirectMessage(request));
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<MessageResponse>> getConversation(@PathVariable UUID userId) {
        return ResponseEntity.ok(directMessageService.getConversation(userId));
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations() {
        return ResponseEntity.ok(directMessageService.getUserConversations());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        directMessageService.deleteDirectMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
