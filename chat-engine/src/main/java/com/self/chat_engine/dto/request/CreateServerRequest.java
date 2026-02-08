package com.self.chat_engine.dto.request;

import com.self.chat_engine.model.enums.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateServerRequest {

    @NotBlank(message = "Server name is required")
    @Size(min = 1, max = 100, message = "Server name must be between 1 and 100 characters")
    private String name;

    private String imageUrl;
}
