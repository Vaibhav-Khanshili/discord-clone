package com.self.chat_engine.services;

import com.self.chat_engine.dto.request.CreateChannelRequest;
import com.self.chat_engine.dto.response.ChannelResponse;
import com.self.chat_engine.exception.ResourceNotFoundException;
import com.self.chat_engine.exception.UnauthorizedException;
import com.self.chat_engine.model.Channel;
import com.self.chat_engine.model.Member;
import com.self.chat_engine.model.Server;
import com.self.chat_engine.model.User;
import com.self.chat_engine.repository.ChannelRepository;
import com.self.chat_engine.repository.MemberRepository;
import com.self.chat_engine.repository.ServerRepository;
import com.self.chat_engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    /**
     * Create a new channel in a server
     * Only moderators and above can create channels
     */
    @Transactional
    public ChannelResponse createChannel(UUID serverId, CreateChannelRequest request, String username) {
        // Get server
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Server", "id", serverId));

        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Check if user is moderator or above
        Member member = memberRepository.findByUserIdAndServerId(user.getId(), serverId)
                .orElseThrow(() -> new UnauthorizedException("You are not a member of this server"));

        if (!member.isModerator()) {
            throw new UnauthorizedException("Only moderators and above can create channels");
        }

        // Create channel
        Channel channel = Channel.builder()
                .name(request.getName())
                .type(request.getType())
                .server(server)
                .build();

        channel = channelRepository.save(channel);

        return mapToChannelResponse(channel);
    }

    /**
     * Get all channels in a server
     */
    @Transactional(readOnly = true)
    public List<ChannelResponse> getServerChannels(UUID serverId, String username) {
        // Verify server exists
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Server", "id", serverId));

        // Verify user is member
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (!memberRepository.existsByUserIdAndServerId(user.getId(), serverId)) {
            throw new UnauthorizedException("You are not a member of this server");
        }

        // Get channels
        List<Channel> channels = channelRepository.findAllByServerIdOrderByCreatedAtAsc(serverId);

        return channels.stream()
                .map(this::mapToChannelResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get channel by ID
     */
    @Transactional(readOnly = true)
    public ChannelResponse getChannelById(UUID channelId, String username) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));

        // Verify user is member of server
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (!memberRepository.existsByUserIdAndServerId(user.getId(), channel.getServer().getId())) {
            throw new UnauthorizedException("You are not a member of this server");
        }

        return mapToChannelResponse(channel);
    }

    /**
     * Update channel name
     * Only moderators and above can update
     */
    @Transactional
    public ChannelResponse updateChannel(UUID channelId, CreateChannelRequest request, String username) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Check permissions
        Member member = memberRepository.findByUserIdAndServerId(user.getId(), channel.getServer().getId())
                .orElseThrow(() -> new UnauthorizedException("You are not a member of this server"));

        if (!member.isModerator()) {
            throw new UnauthorizedException("Only moderators and above can update channels");
        }

        // Update channel
        channel.setName(request.getName());
        if (request.getType() != null) {
            channel.setType(request.getType());
        }

        channel = channelRepository.save(channel);

        return mapToChannelResponse(channel);
    }

    /**
     * Delete a channel
     * Only moderators and above can delete
     * Cannot delete if it's the last channel
     */
    @Transactional
    public void deleteChannel(UUID channelId, String username) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Check permissions
        Member member = memberRepository.findByUserIdAndServerId(user.getId(), channel.getServer().getId())
                .orElseThrow(() -> new UnauthorizedException("You are not a member of this server"));

        if (!member.isModerator()) {
            throw new UnauthorizedException("Only moderators and above can delete channels");
        }

        // Check if it's the last channel
        long channelCount = channelRepository.countByServerId(channel.getServer().getId());
        if (channelCount <= 1) {
            throw new UnauthorizedException("Cannot delete the last channel in the server");
        }

        channelRepository.delete(channel);
    }

    // Helper method
    private ChannelResponse mapToChannelResponse(Channel channel) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .serverId(channel.getServer().getId())
                .createdAt(channel.getCreatedAt())
                .build();
    }
}