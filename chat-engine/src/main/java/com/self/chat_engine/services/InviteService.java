package com.self.chat_engine.services;

import com.self.chat_engine.dto.response.InviteResponse;
import com.self.chat_engine.dto.response.ServerResponse;
import com.self.chat_engine.exception.ResourceNotFoundException;
import com.self.chat_engine.model.Invite;
import com.self.chat_engine.model.Member;
import com.self.chat_engine.model.Server;
import com.self.chat_engine.model.User;
import com.self.chat_engine.repository.InviteRepository;
import com.self.chat_engine.repository.MemberRepository;
import com.self.chat_engine.repository.ServerRepository;
import com.self.chat_engine.repository.UserRepository;
import com.self.chat_engine.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final ServerRepository serverRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ServerService serverService;

    @Transactional
    public InviteResponse createInvite(UUID serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Server", "id", serverId));

        String code = UUID.randomUUID().toString().substring(0, 8);

        Invite invite = Invite.builder()
                .code(code)
                .server(server)
                .build();

        invite = inviteRepository.save(invite);

        return InviteResponse.builder()
                .code(invite.getCode())
                .serverId(server.getId())
                .serverName(server.getName())
                .createdAt(invite.getCreatedAt())
                .build();
    }

    public InviteResponse getInviteByCode(String code) {
        Invite invite = inviteRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invite", "code", code));

        return InviteResponse.builder()
                .code(invite.getCode())
                .serverId(invite.getServer().getId())
                .serverName(invite.getServer().getName())
                .createdAt(invite.getCreatedAt())
                .build();
    }

    @Transactional
    public ServerResponse joinServerByInvite(String code) {
        Invite invite = inviteRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invite", "code", code));

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));

        Server server = invite.getServer();

        if (memberRepository.existsByUserIdAndServerId(user.getId(), server.getId())) {
            throw new RuntimeException("You are already a member of this server");
        }

        Member member = Member.builder()
                .user(user)
                .server(server)
                .role("MEMBER")
                .build();

        memberRepository.save(member);

        return serverService.getServerById(server.getId());
    }

    @Transactional
    public void deleteInvite(String code) {
        Invite invite = inviteRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invite", "code", code));
        inviteRepository.delete(invite);
    }
}
