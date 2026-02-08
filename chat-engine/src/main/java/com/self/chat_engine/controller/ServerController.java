package com.self.chat_engine.controller;

import com.self.chat_engine.dto.request.CreateServerRequest;
import com.self.chat_engine.dto.response.ServerResponse;
import com.self.chat_engine.services.ServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/servers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class ServerController {
    private final ServerService serverService;

    @PostMapping
    public ResponseEntity<ServerResponse> createServer(@Valid @RequestBody CreateServerRequest request) {
        return ResponseEntity.ok(serverService.createServer(request));
    }

    @GetMapping
    public ResponseEntity<List<ServerResponse>> getUserServers() {
        return ResponseEntity.ok(serverService.getUserServers());
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<ServerResponse> getServer(@PathVariable UUID serverId) {
        return ResponseEntity.ok(serverService.getServerById(serverId));
    }

    @DeleteMapping("/{serverId}")
    public ResponseEntity<Void> deleteServer(@PathVariable UUID serverId) {
        serverService.deleteServer(serverId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{serverId}")
    public ResponseEntity<ServerResponse> updateServer(
            @PathVariable UUID serverId,
            @Valid @RequestBody CreateServerRequest request) {
        return ResponseEntity.ok(serverService.updateServer(serverId, request));
    }
}
