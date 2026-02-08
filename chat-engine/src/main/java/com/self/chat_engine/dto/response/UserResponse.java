package com.self.chat_engine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private String imageUrl;
    private String status;
    private String avatar;
    private Date createdAt;
}
