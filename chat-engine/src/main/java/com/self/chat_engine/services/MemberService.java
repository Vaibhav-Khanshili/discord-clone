package com.self.chat_engine.services;

import com.self.chat_engine.dto.request.UpdateMemberRoleRequest;
import com.self.chat_engine.dto.response.MemberResponse;
import com.self.chat_engine.exception.ResourceNotFoundException;
import com.self.chat_engine.exception.UnauthorizedException;
import com.self.chat_engine.model.Member;
import com.self.chat_engine.repository. MemberRepository;
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
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberResponse> getServerMembers(UUID serverId) {
        return memberRepository.findByServerId(serverId).stream()
                .map(this::mapToMemberResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponse updateMemberRole(UUID memberId, UpdateMemberRoleRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));

        CustomUserDetails userDetails = getCurrentUser();

        if (!member.getServer().getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("Only server owner can update member roles");
        }

        member.setRole(request.getRole());
        member = memberRepository.save(member);

        return mapToMemberResponse(member);
    }

    @Transactional
    public void kickMember(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));

        CustomUserDetails userDetails = getCurrentUser();

        if (!member.getServer().getOwner().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("Only server owner can kick members");
        }

        if (member.getUser().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Server owner cannot leave their own server");
        }

        memberRepository.delete(member);
    }

    @Transactional
    public void leaveServer(UUID serverId) {
        CustomUserDetails userDetails = getCurrentUser();

        Member member = memberRepository.findByUserIdAndServerId(userDetails.getId(), serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));

        if (member.getServer().getOwner().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Server owner cannot leave their own server");
        }

        memberRepository.delete(member);
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .userId(member.getUser().getId())
                .username(member.getUser().getUsername())
                .avatar(member.getUser().getImageUrl())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
