package com.self.chat_engine.controller;

import com.self.chat_engine.dto.request.CreateChannelRequest;
import com.self.chat_engine.dto.response.ChannelResponse;
import com.self.chat_engine.services.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<ChannelResponse> createChannel(@Valid @RequestBody CreateChannelRequest request) {
        return ResponseEntity.ok(channelService.createChannel(request));
    }

    @GetMapping("/server/{serverId}")
    public ResponseEntity<List<ChannelResponse>> getServerChannels(@PathVariable UUID serverId) {
        return ResponseEntity.ok(channelService.getChannelsByServerId(serverId));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> getChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelService.getChannelById(channelId));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updateChannel(
            @PathVariable UUID channelId,
            @Valid @RequestBody CreateChannelRequest request) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, request));
    }
}
