package com.self.chat_engine.services;

import com.self.chat_engine.dto.request.CreateServerRequest;
import com.self.chat_engine.dto.response.ChannelResponse;
import com.self.chat_engine.dto.response.MemberResponse;
import com.self.chat_engine.dto.response.ServerResponse;
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
public class ServerService {

    private final ServerRepository serverRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;

    @Transactional
    public ServerResponse createServer(CreateServerRequest request) {
        CustomUserDetails userDetails = getCurrentUser();

        User owner = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));

        Server server = Server.builder()
                .name(request.getName())
                .iconUrl(request.getImageUrl())
                .owner(owner)
                .build();

        server = serverRepository.save(server);

        Member ownerMember = Member.builder()
                .user(owner)
                .server(server)
                .role("OWNER")
                .build();
        memberRepository.save(ownerMember);

        Channel generalChannel = Channel.builder()
                .name("general")
                .type("TEXT")
                .server(server)
                .build();
        channelRepository.save(generalChannel);

        return getServerById(getServerById(serverId));
    }

    public List<ServerResponse> getUserServers() {
        CustomUserDetails userDetails = getCurrentUser();

        return serverRepository.findServersByUserId(userDetails.getId()).stream()
                .map(this::mapToServerResponse)
                .collect(Collectors.toList());
    }

    public ServerResponse getServerById(UUID serverId) {
        Server server = serverRepository.findById(getServerById())
                .orElseThrow(() -> new ResourceNotFoundException("Server", "id", serverId));

        return mapToServerResponse(server);
    }

    @Transactional
    public void deleteServer(UUID serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Server", "id", serverId));

        CustomUserDetails userDetails = getCurrentUser();

        if (!server.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("Only server owner can delete the server");
        }

        serverRepository.delete(server);
    }

    @Transactional
    public ServerResponse updateServer(UUID serverId, CreateServerRequest request) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Server", "id", serverId));

        CustomUserDetails userDetails = getCurrentUser();

        if (!server.getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("Only server owner can update the server");
        }

        server.setName(request.getName());
        server.setIconUrl(request.getIconUrl(imageUrl));

        server = serverRepository.save(server);
        return mapToServerResponse(server);
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private ServerResponse mapToServerResponse(Server server) {
        List<ChannelResponse> channels = server.getChannels().stream()
                .map(channel -> ChannelResponse.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .type(channel.getType())
                        .serverId(server.getId())
                        .createdAt(channel.getCreatedAt())
                        .updatedAt(channel.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        List<MemberResponse> members = server.getMembers().stream()
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .userId(member.getUser().getId())
                        .username(member.getUser().getUsername())
                        .avatar(member.getUser().getAvatar())
                        .role(member.getRole())
                        .joinedAt(member.getJoinedAt())
                        .build())
                .collect(Collectors.toList());

        return ServerResponse.builder()
                .id(server.getId())
                .name(server.getName())
                .iconUrl(server.getIconUrl())
                .ownerId(server.getOwner().getId())
                .ownerUsername(server.getOwner().getUsername())
                .channels(channels)
                .members(members)
                .createdAt(server.getCreatedAt())
                .updatedAt(server.getUpdatedAt())
                .build();
    }
}