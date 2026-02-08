package com.self.chat_engine.services;

import com.self.chat_engine.dto.request.SendMessageRequest;
import com.self.chat_engine.dto.response.MessageResponse;
import com.self.chat_engine.exception.ResourceNotFoundException;
import com.self.chat_engine.model.Channel;
import com.self.chat_engine.model.Message;
import com.self.chat_engine.model.User;
import com.self.chat_engine.repository.ChannelRepository;
import com.self.chat_engine.repository.MessageRepository;
import com.self.chat_engine.repository.UserRepository;
import com.self.chat_engine.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request) {
        CustomUserDetails userDetails = getCurrentUser();

        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", request.getChannelId()));

        User sender = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));

        Message message = Message.builder()
                .content(request.getContent())
                .channel(channel)
                .sender(sender)
                .messageType(request.getMessageType())
                .fileUrl(request.getFileUrl())
                .fileName(request.getFileName())
                .build();

        message = messageRepository.save(message);
        return mapToMessageResponse(message);
    }

    public List<MessageResponse> getMessagesByChannelId(UUID channelId) {
        return messageRepository.findByChannelIdAndDeletedFalseOrderByCreatedAtAsc(channelId)
                .stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        CustomUserDetails userDetails = getCurrentUser();

        if (!message.getSender().getId().equals(userDetails.getId())) {
            throw new RuntimeException("You can only delete your own messages");
        }

        message.setDeleted(true);
        messageRepository.save(message);
    }

    @Transactional
    public MessageResponse updateMessage(UUID messageId, SendMessageRequest request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        CustomUserDetails userDetails = getCurrentUser();

        if (!message.getSender().getId().equals(userDetails.getId())) {
            throw new RuntimeException("You can only edit your own messages");
        }

        message.setContent(request.getContent());
        message.setEdited(true);

        message = messageRepository.save(message);
        return mapToMessageResponse(message);
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .channelId(message.getChannel().getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .senderAvatar(message.getSender().getAvatar())
                .messageType(message.getMessageType())
                .fileUrl(message.getFileUrl())
                .fileName(message.getFileName())
                .edited(message.isEdited())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
