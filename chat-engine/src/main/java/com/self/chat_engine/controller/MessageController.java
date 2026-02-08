package com.self.chat_engine.controller;

import com.self.chat_engine.dto.request.UpdateMemberRoleRequest;
import com.self.chat_engine.dto.response.MemberResponse;
import com.self.chat_engine.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class MessageController {

    private final MemberService memberService;

    @GetMapping("/server/{serverId}")
    public ResponseEntity<List<MemberResponse>> getServerMembers(@PathVariable UUID serverId) {
        return ResponseEntity.ok(memberService.getServerMembers(serverId));
    }

    @PutMapping("/{memberId}/role")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.ok(memberService.updateMemberRole(memberId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> kickMember(@PathVariable UUID memberId) {
        memberService.kickMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/server/{serverId}/leave")
    public ResponseEntity<Void> leaveServer(@PathVariable UUID serverId) {
        memberService.leaveServer(serverId);
        return ResponseEntity.noContent().build();
    }
}
