package com.self.chat_engine.dto.request;

import com.self.chat_engine.model.enums.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateChannelRequest {

    @NotBlank(message = "Channel name is required")
    @Size(min = 1, max = 100, message = "Channel name must be between 1 and 100 characters")
    private String name;
    private UUID serverId;

    private ChannelType type = ChannelType.TEXT;

}
