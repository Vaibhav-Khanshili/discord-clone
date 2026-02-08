package com.self.chat_engine.controller;

import com.self.chat_engine.dto.response.InviteResponse;
import com.self.chat_engine.dto.response.ServerResponse;
import com.self.chat_engine.services.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class InviteController {
    private final InviteService inviteService;

    @PostMapping("/server/{serverId}")
    public ResponseEntity<InviteResponse> createInvite(@PathVariable UUID serverId) {
        return ResponseEntity.ok(inviteService.createInvite(serverId));
    }

    @GetMapping("/{inviteCode}")
    public ResponseEntity<InviteResponse> getInvite(@PathVariable String inviteCode) {
        return ResponseEntity.ok(inviteService.getInviteByCode(inviteCode));
    }

    @PostMapping("/{inviteCode}/join")
    public ResponseEntity<ServerResponse> joinServerByInvite(@PathVariable String inviteCode) {
        return ResponseEntity.ok(inviteService.joinServerByInvite(inviteCode));
    }

    @DeleteMapping("/{inviteCode}")
    public ResponseEntity<Void> deleteInvite(@PathVariable String inviteCode) {
        inviteService.deleteInvite(inviteCode);
        return ResponseEntity.noContent().build();
    }
}
