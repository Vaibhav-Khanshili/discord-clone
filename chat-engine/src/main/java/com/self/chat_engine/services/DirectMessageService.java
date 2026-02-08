package com.self.chat_engine.services;

import com.self.chat_engine.dto.request.SendMessageRequest;
import com.self.chat_engine.dto.response.MessageResponse;
import com.self.chat_engine.exception.ResourceNotFoundException;
import com.self.chat_engine.model.DirectMessage;
import com.self.chat_engine.model.User;
import com.self.chat_engine.repository.DirectMessageRepository;
import com.self.chat_engine.repository.UserRepository;
import com.self.chat_engine.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse sendDirectMessage(SendMessageRequest request) {
        CustomUserDetails userDetails = getCurrentUser();

        User sender = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));

        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getRecipientId()));

        DirectMessage message = DirectMessage.builder()
                .content(request.getContent())
                .sender(sender)
                .recipient(recipient)
                .messageType(request.getMessageType())
                .fileUrl(request.getFileUrl())
                .fileName(request.getFileName())
                .build();

        message = directMessageRepository.save(message);
        return mapToMessageResponse(message);
    }

    public List<MessageResponse> getConversation(UUID userId) {
        CustomUserDetails userDetails = getCurrentUser();

        return directMessageRepository.findConversation(userDetails.getId(), userId).stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    public List<Object> getUserConversations() {
        CustomUserDetails userDetails = getCurrentUser();
        return directMessageRepository.findUserConversations(userDetails.getId());
    }

    @Transactional
    public void deleteDirectMessage(UUID messageId) {
        DirectMessage message = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        CustomUserDetails userDetails = getCurrentUser();
        if (!message.getSender().getId().equals(userDetails.getId())) {
            throw new RuntimeException("You can only delete your own messages");
        }

        message.setDeleted(true);
        directMessageRepository.save(message);
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private MessageResponse mapToMessageResponse(DirectMessage message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .senderAvatar(message.getSender().getAvatar())
                .messageType(message.getMessageType())
                .fileUrl(message.getFileUrl())
                .fileName(message.getFileName())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
